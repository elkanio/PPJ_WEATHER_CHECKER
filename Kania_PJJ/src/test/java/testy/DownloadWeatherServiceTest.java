package testy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ppj.weather.Main;
import ppj.weather.model.City;
import ppj.weather.model.State;
import ppj.weather.model.WeatherRecord;
import ppj.weather.services.CityService;
import ppj.weather.services.DownloadWeatherService;
import ppj.weather.services.StateService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@ActiveProfiles({"test"})
@SpringBootTest
public class DownloadWeatherServiceTest {

    @Autowired
    DownloadWeatherService downloadWeatherService;

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @Test
    public void testDownloadRawWeatherForCity() throws IOException {
        State state = new State("Czech Republic");
        stateService.create(state);

        City city1 = cityService.create(new City("Praha", state));
        City city2 = cityService.create(new City("Liberec", state));

        List<City> cities = cityService.getAll();

        String rawResult = downloadWeatherService.downloadRawWeatherForCity(cities.get(0));

        assertNotNull("Result should not be null", rawResult);
        assertTrue("Result should contain temperature", rawResult.contains("temp"));
    }

    @Test
    public void testParseRawStringToWeatherRecord() {
        String inputJson = "{\"coord\":{\"lon\":14.42,\"lat\":50.09},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":20,\"pressure\":1009,\"humidity\":60,\"temp_min\":20,\"temp_max\":21},\"visibility\":10000,\"wind\":{\"speed\":2.6,\"deg\":340},\"clouds\":{\"all\":75},\"dt\":1528808400,\"sys\":{\"type\":1,\"id\":5889,\"message\":0.0024,\"country\":\"CZ\",\"sunrise\":1528771947,\"sunset\":1528830742},\"id\":3067696,\"name\":\"Prague\",\"cod\":200}";

        State state = new State("Czech Republic");
        stateService.create(state);

        City city1 = cityService.create(new City("Praha", state));
        City city2 = cityService.create(new City("Liberec", state));

        WeatherRecord weatherRecord = downloadWeatherService.parseRawStringToWeatherRecord(inputJson, city1);

        assertNotNull("Should be parsed record", weatherRecord);
        assertEquals("Temp should be 20", "20",  String.valueOf((int)weatherRecord.getTemperature()));
    }
}
