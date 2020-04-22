package ppj.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import ppj.weather.configuration.CSVConfiguration;
import ppj.weather.model.StatisticExt;
import ppj.weather.model.City;
import ppj.weather.model.State;
import ppj.weather.model.WeatherRecord;
import ppj.weather.model.LatestWeather;
import ppj.weather.model.WeatherAverages;
import ppj.weather.model.Intervals;
import ppj.weather.repositories.CityRepository;
import ppj.weather.repositories.StateRepository;
import ppj.weather.repositories.WeatherRecordRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherRecordService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;

    private final WeatherRecordRepository repository;

    private final MongoTemplate mongoTemplate;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyyy");
    private CSVConfiguration csvConfiguration;

    @Autowired
    public WeatherRecordService(CityRepository cityRepository, WeatherRecordRepository repository,
                                MongoTemplate mongoTemplate, StateRepository stateRepository) {

        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.csvConfiguration = new CSVConfiguration();
    }

    public long getCount() {
        return repository.count();
    }

    public WeatherRecord create(WeatherRecord weatherRecord) {
        return repository.save(weatherRecord);
    }

    public List<WeatherRecord> create(List<WeatherRecord> weatherRecords) {
        return repository.saveAll(weatherRecords);
    }

    public List<WeatherRecord> getAll() {
        return repository.findAll();
    }

    public void delete(WeatherRecord weatherRecord) {
        repository.delete(weatherRecord);
    }

    public Optional<WeatherRecord> get(String id) {
        return repository.findById(id) ;
    }

    public WeatherRecord save(WeatherRecord weatherRecord) {
        return repository.save(weatherRecord);
    }

    public List<WeatherRecord> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    public List<LatestWeather> getCitiesWithLatestWeatherForState(int idState) {
        List<City> cities = cityRepository.findAllByState_Id(idState);

        List<LatestWeather> result = new ArrayList<>();
        cities.forEach((city -> {
            WeatherRecord latest = this.findLatestWeatherRecordForCity(city.getId());

            if(latest != null) {
                result.add(new LatestWeather(city, latest));
            }
        }));

        return result;
    }

    private WeatherRecord findLatestWeatherRecordForCity(int id) {
        return repository.findFirstByCityIdOrderByIdDesc(id);
    }

    public boolean exists(String id) {
        return repository.existsById(id);
    }

    public int getWeatherRecordCount() {
        return (int) repository.count();
    }

    public LatestWeather getLatestWeatherRecordForCity(int idCity) {
        Optional<City> city = cityRepository.findById(idCity);
        if(city.isPresent() == false) {
            return null;
        }

        WeatherRecord latest = this.findLatestWeatherRecordForCity(idCity);
        if(latest == null) {
            return null;
        }

        return new LatestWeather(city.get(), latest);
    }

    public WeatherAverages getAverageWeatherForCity(int idCity, Intervals interval) {
        if(cityRepository.existsById(idCity) == false) {
            return null;
        }

        if(repository.findFirstByCityIdOrderByIdDesc(idCity) == null) {
            return null;
        }

        Date dateRange = StatisticExt.getDateRangeFromNowByInterval(interval);

        GroupOperation groupOperation = Aggregation.group()
                .avg(WeatherRecord.HUMIDITY_NAME).as(WeatherAverages.HUMIDITY_NAME)
                .avg(WeatherRecord.PRECIPITATION_NAME).as(WeatherAverages.PRECIPITATION_NAME)
                .avg(WeatherRecord.TEMPERATURE_NAME).as(WeatherAverages.TEMPERATURE_NAME);

        MatchOperation matchOperation = Aggregation.match(new Criteria(WeatherRecord.DATE_NAME)
                                                                .gt(dateRange)
                                                                .and(WeatherRecord.CITY_ID_NAME)
                                                                .is(idCity));

        ProjectionOperation projectionOperation = Aggregation.project(WeatherAverages.HUMIDITY_NAME,
                                                                      WeatherAverages.PRECIPITATION_NAME,
                                                                      WeatherAverages.TEMPERATURE_NAME);

        projectionOperation = projectionOperation.andExclude(WeatherRecord.ID_NAME);

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation, projectionOperation);

        AggregationResults<WeatherAverages> result = mongoTemplate.aggregate(aggregation,
                                                                                     WeatherRecord.COLLECTION_NAME,
                                                                                     WeatherAverages.class);

        return result.getUniqueMappedResult();
    }

    public void importFile(MultipartFile multipartFile, String stateName, String cityName) throws RuntimeException {
        if(multipartFile == null) {
            return;
        }

        State state = stateRepository.findByName(stateName);
        if (state  == null) {
            throw new RuntimeException (stateName+" v DB není.");
        }

        //existuje dane mesto v dané zemy?

        int id = state.getId();
        List<City> citiesInCurrentCountry = cityRepository.findAllByState_Id(id);

        if (citiesInCurrentCountry.size() < 1) {

            throw new RuntimeException ("Mesto neni ulozene v DB pro dany stat.");

        }

        City city = cityRepository.findByName(cityName);

        try {
            String csvFileContent = new String(multipartFile.getBytes());
            List<RowsFromCSV> rowsFromCSVS = csvConfiguration.parseFile(csvFileContent);

            //každý řádek CSV souboru
            for (RowsFromCSV row : rowsFromCSVS) {
                WeatherRecord weatherRecord = new WeatherRecord();
                weatherRecord.setCity(city);
                weatherRecord.setCityId(city.getId());
                weatherRecord.setTemperature(row.getTemperature());
                weatherRecord.setHumidity(row.getHumidity());
                weatherRecord.setPrecipitation(row.getPrecipitation());
                weatherRecord.setDate(row.getDate());
                repository.save(weatherRecord);
            }

        } catch (IOException e) {
            throw new RuntimeException ("Problém s ukladanim mesto do monga DB");
        }
    }

    public String exportToFile(String stateName, String cityName) throws RuntimeException  {
        State state = stateRepository.findByName(stateName);
        if (state == null) {
            throw new RuntimeException ("stat " + stateName + " neexistuje");
        }

        int id = state.getId();
        List<City> citiesInCurrentCountry = cityRepository.findAllByState_Id(id);

        if (citiesInCurrentCountry.size() < 1) {

            throw new RuntimeException ("Mesto neni ulozene v DB pro dany stat.");

        }

        City city = cityRepository.findByName(cityName);

        List<WeatherRecord> records = repository.findAllByCity_Id(city.getId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(csvConfiguration.getHeaderForCSV());
        for (WeatherRecord weatherRecord : records) {
            stringBuilder.append(DATE_FORMAT.format(weatherRecord.getDate()));
            stringBuilder.append(";");
            stringBuilder.append(weatherRecord.getHumidity());
            stringBuilder.append(weatherRecord.getPrecipitation());
            stringBuilder.append(weatherRecord.getTemperature());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
