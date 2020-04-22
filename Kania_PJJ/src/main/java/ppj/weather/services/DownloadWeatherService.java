package ppj.weather.services;

import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ppj.weather.configuration.WeatherSourceConfiguration;
import ppj.weather.model.City;
import ppj.weather.model.WeatherRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class DownloadWeatherService {

    private static final Logger log = LoggerFactory.getLogger(DownloadWeatherService.class);

    private final static String CITY_PAR_NAME = "{CITY_NAME}";

    private final static String API_KEY_PAR_NAME = "{API_KEY}";

    private final static int CONN_TIMEOUT = 7000;

    private final static int READ_TIMEOUT = 7000;

    private StateService stateService;

    private CityService cityService;

    private WeatherRecordService weatherRecordService;

    private WeatherSourceConfiguration weatherSourceConfiguration;

    private ThreadPoolTaskScheduler weatherThreadPoolTaskScheduler;

    @Autowired
    public DownloadWeatherService(StateService stateService, CityService cityService,
                                  WeatherRecordService weatherRecordService, WeatherSourceConfiguration weatherSourceConfiguration,
                                  ThreadPoolTaskScheduler weatherThreadPoolTaskScheduler) {

        this.stateService = stateService;
        this.cityService = cityService;
        this.weatherRecordService = weatherRecordService;
        this.weatherSourceConfiguration = weatherSourceConfiguration;
        this.weatherThreadPoolTaskScheduler = weatherThreadPoolTaskScheduler;

        startWeatherDownloadingScheduler();
    }

    private void startWeatherDownloadingScheduler() {
        weatherThreadPoolTaskScheduler.scheduleAtFixedRate(this::downloadWeatherForCities,
                (weatherSourceConfiguration.getUpdateRate() * 1000));

        log.info("Downloading task is scheduled.");
    }

    private void downloadWeatherForCities() {
        List<City> cities = cityService.getAll();

        for (City city:
             cities) {
                downloadWeatherForCity(city);
        }
    }

    private void downloadWeatherForCity(City city) {
        try {
            String rawResult = downloadRawWeatherForCity(city);
            WeatherRecord parsedResult = parseRawStringToWeatherRecord(rawResult, city);

            weatherRecordService.save(parsedResult);
        } catch (Exception ex) {
            log.error("Problém se stahováním dat z webu pro mesto: " + city.getId(), ex);
        }
    }

    public WeatherRecord parseRawStringToWeatherRecord(String rawResult, City city) {
        WeatherRecord weatherRecord = new WeatherRecord();

        weatherRecord.setCity(city);
        weatherRecord.setCityId(city.getId());
        weatherRecord.setTemperature(parseJsonNumber(JsonPath.read(rawResult, "$.main.temp")));
        weatherRecord.setHumidity(parseJsonNumber(JsonPath.read(rawResult, "$.main.humidity")));
        weatherRecord.setPrecipitation(parseJsonNumber(JsonPath.read(rawResult, "$.main.pressure")));
        weatherRecord.setDate(new Date());

        return weatherRecord;
    }

    private double parseJsonNumber(Object jsonNumber) {
        if(jsonNumber instanceof Double) {
            return  (double) jsonNumber;
        } else if(jsonNumber instanceof Integer) {
            return ((Integer) jsonNumber).doubleValue();
        }

        log.error("Unable to parse " + jsonNumber + " to any number.");

        throw new IllegalArgumentException("Invalid number format: " + jsonNumber);
    }

    public String downloadRawWeatherForCity(City city) throws IOException {
        String composedUrl = composeURLForCity(city);
        URL url = new URL(composedUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONN_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        return builder.toString();
    }

    private String composeURLForCity(City city) {
        return weatherSourceConfiguration.getWeatherUrl().replace(CITY_PAR_NAME, String.valueOf(city.getName()))
                                                  .replace(API_KEY_PAR_NAME, weatherSourceConfiguration.getApiKey());
    }
}
