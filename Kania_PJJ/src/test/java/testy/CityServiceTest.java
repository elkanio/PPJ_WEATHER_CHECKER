package testy;

import org.junit.Before;
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
import ppj.weather.services.CityService;
import ppj.weather.services.StateService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@ActiveProfiles({"test"})
@SpringBootTest
public class CityServiceTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    private State state1 = new State( "UK");
    private State state2 = new State( "Czech Republic");


    private City city1 = new City("Liberec", state2);
    private City city2 = new City("Praha", state2);
    private City city3 = new City("London", state1);

    @Before
    public void init() {
        cityService.deleteCities();
    }

    @Test
    public void testCreateRetrieve() {
        stateService.create(state1);
        stateService.create(state2);

        cityService.save(city1);

        List<City> cities1 = cityService.get();
        assertEquals("Should be one city.", 1, cities1.size());

        assertEquals("Retrieved city should equal inserted city", city1.toString(),
                cities1.get(0).toString());

        cityService.save(city2);
        cityService.save(city3);

        List<City> cities = cityService.get();
        assertEquals("Should be three cities for states.", 3,
                cities.size());
    }

    @Test
    public void testDelete() {
        stateService.create(state1);
        stateService.create(state2);


        cityService.save(city1);
        cityService.save(city2);
        cityService.save(city3);

        Optional<City> retrieved1 = cityService.get(city1.getId());
        assertTrue("Some is found", retrieved1.isPresent());

        cityService.delete(city2.getId());
        Optional<City> retrieved2 = cityService.get(city2.getId());
        assertFalse("Deleted city is not present", retrieved2.isPresent());

        cityService.delete(city3);
        Optional<City> retrieved3 = cityService.get(city3.getId());
        assertFalse("Deleted city is not present", retrieved3.isPresent());
    }

    @Test
    public void testGetById() {
        stateService.create(state1);
        stateService.create(state2);

        cityService.save(city1);
        cityService.save(city2);
        cityService.save(city3);

        Optional<City> retrieved1 = cityService.get(city1.getId());
        assertTrue("Some is found", retrieved1.isPresent());

        Optional<City> retrieved2 = cityService.get(city3.getId());
        assertFalse("City is not present", retrieved2.isPresent());
    }

    @Test
    public void testUpdate() {
        stateService.create(state1);
        stateService.create(state2);


        cityService.save(city1);
        cityService.save(city2);
        cityService.save(city3);

        city3.setName("Berlin");
        cityService.save(city3);

        City retrieved = cityService.get(city3.getId()).get();
        assertEquals("Retrieved city should be updated", retrieved.toString(), city3.toString());
    }

    @Test
    public void testGetCitiesCount() {
        stateService.create(state1);
        stateService.create(state2);

        cityService.save(city1);
        cityService.save(city2);
        cityService.save(city3);

        int result = cityService.getCitiesCount();

        assertTrue("Should be equals 3", result == 3);
    }

}
