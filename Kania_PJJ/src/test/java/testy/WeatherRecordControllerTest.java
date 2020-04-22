package testy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ppj.weather.Main;
import ppj.weather.model.City;
import ppj.weather.model.State;
import ppj.weather.model.WeatherRecord;
import ppj.weather.services.CityService;
import ppj.weather.services.StateService;
import ppj.weather.services.WeatherRecordService;
import ppj.weather.web.Api;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"test"})
public class WeatherRecordControllerTest {

    private final String TEST_URL = "http://localhost:8080";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(TEST_URL)
            .addConverterFactory(JacksonConverterFactory.create()).build();

    private Api restService = retrofit.create(Api.class);

    @Autowired
    private WeatherRecordService weatherRecordService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    private State state1 = new State("Czech Republic");
    private State state2 = new State("USA");
    private State state3 = new State("RSA");

    private City city1   = new City("Liberec", state1);
    private City city2   = new City("Praha", state1);
    private City city3   = new City("New York", state2);
    private City city4   = new City("Cape Town", state3);

    @Test
    public void testCreateWeatherRecord() throws IOException {
        createStatesAndCities();
        WeatherRecord record = new WeatherRecord(1, 20, 20, 20);

        int previousCount = weatherRecordService.getWeatherRecordCount();

        Response<WeatherRecord> response = restService.addWeatherRecord(record).execute();

        int currentCount = weatherRecordService.getWeatherRecordCount();
        assertTrue("Count should be one more than previous", (previousCount + 1) == currentCount);

        WeatherRecord weatherRecord = weatherRecordService.get(response.body().getId()).get();
        assertNotNull("Should be present", weatherRecord);
    }

    @Test
    public void testDeleteWeatherRecord() throws IOException {
        createStatesAndCities();
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);

        restService.deleteWeatherRecord(record.getId()).execute();

        Optional<WeatherRecord> result = weatherRecordService.get(record.getId());

        assertFalse("Should not be present", result.isPresent());
    }

    @Test
    public void testUpdateWeatherRecord() throws IOException {
        createStatesAndCities();
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);

        WeatherRecord toUpdate = weatherRecordService.get(record.getId()).get();
        record.setTemperature(30);

        Response<WeatherRecord> updated = restService.updateWeatherRecord(record).execute();
        assertNotNull("Response should not be null.", updated);

        WeatherRecord result = weatherRecordService.get(record.getId()).get();
        assertEquals("Temp should be updated","30", (int)result.getTemperature() + "");
    }

    @Test
    public void testGetWeatherRecords() throws IOException {
        createStatesAndCities();
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);
        WeatherRecord record3    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record3);
        WeatherRecord record4   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record4);

        int count = weatherRecordService.getWeatherRecordCount();

        Response<List<WeatherRecord>> response = restService.showWeatherRecords().execute();

        assertNotNull("Response shoul not be null", response);
        assertEquals("Count should be the same", count, response.body().size());
        assertEquals("Record4 should be present", response.body().get((response.body().size() - 1)).getId()
                , record4.getId());
    }

    @Test
    public void testGetWeatherRecord() throws IOException {
        createStatesAndCities();
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);

        Response<WeatherRecord> response = restService.getWeatherRecord(record.getId()).execute();

        assertNotNull("Response shoul not be null", response);
        assertEquals("City1 should be present", response.body().getId(), record.getId());
    }

    private void createStatesAndCities() {
        stateService.create(state1);
        stateService.create(state2);
        stateService.create(state3);

        cityService.create(city1);
        cityService.create(city2);
        cityService.create(city3);
        cityService.create(city4);
    }

}
