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
import ppj.weather.services.CityService;
import ppj.weather.services.StateService;
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
public class CityControllerTest {

    private final String TEST_URL = "http://localhost:8080";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(TEST_URL)
            .addConverterFactory(JacksonConverterFactory.create()).build();

    private Api restService = retrofit.create(Api.class);

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    private State state1 = new State("Czech Republic");
    private State state2 = new State("USA");
    private State state3 = new State("RSA");

    private City city1   = new City("Liberec", state1);
    private City city2   = new City("Praha", state1);
    private City city3   = new City("New York", state2);
    private City city4   = new City("Cape Town", state3);

    @Test
    public void testCreateCity() throws IOException {
        createStates();

        int previousCount = cityService.getCitiesCount();

        Response<City> response = restService.addCity(city1).execute();

        int currentCount = cityService.getCitiesCount();
        assertTrue("Count should be one more than previous", (previousCount + 1) == currentCount);

        City state = cityService.get(response.body().getId()).get();
        assertNotNull("Should be present", state);
    }

    @Test
    public void testDeleteCity() throws IOException {
        createStates();
        cityService.create(city1);

        restService.deleteState(state1.getId()).execute();

        Optional<City> result = cityService.get(state1.getId());

        assertFalse("Should not be present", result.isPresent());
    }

    @Test
    public void testUpdateCity() throws IOException {
        createStates();
        cityService.create(city1);

        City toUpdate = cityService.get(city1.getId()).get();
        city1.setName("Liberek");

        Response<City> updated = restService.updateCity(city1).execute();
        assertNotNull("Response should not be null.", updated);

        City result = cityService.get(city1.getId()).get();
        assertEquals("Name should be updated","Liberek",result.getName());
    }

    @Test
    public void testGetCities() throws IOException {
        createStates();
        cityService.create(city1);
        cityService.create(city2);
        cityService.create(city3);

        int count = cityService.getCitiesCount();

        Response<List<City>> response = restService.showCities().execute();

        assertNotNull("Response shoul not be null", response);
        assertEquals("Count should be the same", count, response.body().size());
        assertTrue("City3 should be present", response.body().get((response.body().size() - 1)).getId()
                == city3.getId());
    }

    @Test
    public void testGetCity() throws IOException {
        createStates();
        cityService.create(city1);
        cityService.create(city2);
        cityService.create(city3);

        Response<City> response = restService.getCity(city1.getId()).execute();

        assertNotNull("Response shoul not be null", response);
        assertTrue("City1 should be present", response.body().getId() == city1.getId());
    }

    private void createStates() {
        stateService.create(state1);
        stateService.create(state2);
        stateService.create(state3);
    }
}
