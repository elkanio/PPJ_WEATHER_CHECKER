package testy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ppj.weather.Main;
import ppj.weather.model.City;
import ppj.weather.model.State;
import ppj.weather.model.WeatherRecord;
import ppj.weather.model.LatestWeather;
import ppj.weather.model.WeatherAverages;
import ppj.weather.model.Intervals;
import ppj.weather.services.CityService;
import ppj.weather.services.StateService;
import ppj.weather.services.WeatherRecordService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@ActiveProfiles({"test"})
@SpringBootTest
public class WeatherRecordServiceTest {

    @Autowired
    private WeatherRecordService weatherRecordService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    @Test
    public void testCreateWeatherRecord() {
        WeatherRecord record = new WeatherRecord(1, 20, 20, 20);
        long previous = weatherRecordService.getCount();

        WeatherRecord response = weatherRecordService.create(record);
        long now = weatherRecordService.getCount();

        assertEquals("Record has been inserted", previous, (now - 1));
        assertTrue("Record has been inserted", response.getId() != null);
    }

    @Test
    public void testDeleteWeatherRecord() {
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);

        long previousCount = weatherRecordService.getCount();

        weatherRecordService.delete(record);
        long count = weatherRecordService.getCount();

        assertTrue("Count should be one less", previousCount == (count + 1));

        assertFalse("Removed should not be present", weatherRecordService
                .get(record.getId())
                .isPresent());
    }

    @Test
    public void testUpdateWeatherRecord() {
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);

        WeatherRecord record1   = weatherRecordService.getAll().get(0);
        record1.setTemperature(100);
        weatherRecordService.save(record1);

        assertTrue("Should be present", weatherRecordService.get(record1.getId())
            .isPresent());
        assertTrue("Should be updated", weatherRecordService.get(record1.getId())
            .get().getTemperature() == 100);
    }

    @Test
    public void testGetWeatherRecord() {
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);

        Optional<WeatherRecord> result = weatherRecordService.get(record2.getId());
        assertTrue("Should be present", result.isPresent());
        assertNotNull("Should be not null", result.get());
    }

    @Test
    public void testGetPageableWeatherRecord() {
        WeatherRecord record    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record2);
        WeatherRecord record3    = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record3);
        WeatherRecord record4   = new WeatherRecord(1, 20, 20, 20);
        weatherRecordService.create(record4);

        Iterable<WeatherRecord> result = weatherRecordService.getAll(PageRequest.of(1, 2));
        WeatherRecord[] arrResult = ((List<WeatherRecord>) result).toArray(new WeatherRecord[((List<WeatherRecord>) result).size()]);

        assertTrue("Should be same", arrResult[0].getId().equals(record3.getId()));
        assertTrue("Should be same", arrResult[1].getId().equals(record4.getId()));
    }

    @Test
    public void testGetCitiesWithLatestWeatherForState() {
        State state = new State("Czech Republic");
        stateService.create(state);

        City city1 = cityService.create(new City("Praha", state));
        City city2 = cityService.create(new City("Liberec", state));

        WeatherRecord record4   = new WeatherRecord(city1.getId(), 20, 20, 20);
        weatherRecordService.create(record4);
        WeatherRecord record    = new WeatherRecord(city2.getId(), 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(city2.getId(), 30, 30, 30);
        weatherRecordService.create(record2);

        List<LatestWeather> cityWithLatestWeatherRecords
            = weatherRecordService.getCitiesWithLatestWeatherForState(state.getId());

        assertTrue("Should be more than zero", cityWithLatestWeatherRecords.size() > 0);

        LatestWeather cityWithLatestWeatherRecord = null;
        for (LatestWeather item:
             cityWithLatestWeatherRecords) {
            if(item.getCity().getName().equals(city2.getName())) {
                cityWithLatestWeatherRecord = item;
            }
        }

        assertNotNull(cityWithLatestWeatherRecord);
        assertEquals("temp should be same", (int)record2.getTemperature(), (int)cityWithLatestWeatherRecord.getTemperature());
    }

    @Test
    public void testGetCityWithLatestWeather() {
        State state = new State("Czech Republic");
        stateService.create(state);

        City city2 = cityService.create(new City("Liberec", state));

        WeatherRecord record    = new WeatherRecord(city2.getId(), 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(city2.getId(), 30, 30, 30);
        weatherRecordService.create(record2);

        LatestWeather latestWeather
                = weatherRecordService.getLatestWeatherRecordForCity(city2.getId());

        assertNotNull(latestWeather);
        assertEquals("Temp should be same", (int)record2.getTemperature(), (int) latestWeather.getTemperature());
    }

    @Test
    public void testGetAverageWeatherForCity() {
        State state = new State("Czech Republic");
        stateService.create(state);

        City city2 = cityService.create(new City("Liberec", state));

        WeatherRecord record    = new WeatherRecord(city2.getId(), 20, 20, 20);
        weatherRecordService.create(record);
        WeatherRecord record2   = new WeatherRecord(city2.getId(), 30, 30, 30);
        weatherRecordService.create(record2);

        WeatherAverages weatherAverages
                = weatherRecordService.getAverageWeatherForCity(city2.getId(), Intervals.TWO_WEEKS);

        assertNotNull(weatherAverages);
        assertEquals("Temp should be same", 25, (int) weatherAverages.getTemperature());
    }

    @Before
    public final void setUp() {
        for (WeatherRecord item:
                weatherRecordService.getAll()) {

            weatherRecordService.delete(item);
        }
    }
}
