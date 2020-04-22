package testy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ppj.weather.Main;
import ppj.weather.model.State;
import ppj.weather.services.StateService;
import ppj.weather.web.Api;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Main.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({"test"})
public class StateControllerTest {

    private final String TEST_URL = "http://localhost:8080";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(TEST_URL)
            .addConverterFactory(JacksonConverterFactory.create()).build();

    private Api restService = retrofit.create(Api.class);

    @Autowired
    private StateService stateService;

    private State state1 = new State("Czech Republic");
    private State state2 = new State("USA");
    private State state3 = new State("RSA");


    @Test
    public void testCreateState() throws IOException {
        int previousCount = stateService.getStateCount();

        Response<State> response = restService.addState(state1).execute();

        int currentCount = stateService.getStateCount();
        assertTrue("Count should be one more than previous", (previousCount + 1) == currentCount);

        State state = stateService.get(response.body().getId()).get();
        assertNotNull("Should be present", state);
    }

    @Test
    public void testDeleteState() throws IOException {
        stateService.create(state1);

        restService.deleteState(state1.getId()).execute();

        Optional<State> result = stateService.get(state1.getId());

        assertFalse("Should not be present", result.isPresent());
    }

    @Test
    public void testUpdateState() throws IOException {
        stateService.create(state1);

        State toUpdate = stateService.get(state1.getId()).get();
        state1.setName("UK");

        Response<State> updated = restService.updateState(state1).execute();
        assertNotNull("Response should not be null.", updated);

        State result = stateService.get(state1.getId()).get();
        assertEquals("Name should be updated","UK",result.getName());
    }

    @Test
    public void testGetStates() throws IOException {
        stateService.create(state1);
        stateService.create(state2);
        stateService.create(state3);

        int count = stateService.getStateCount();

        Response<List<State>> response = restService.showStates().execute();

        assertNotNull("Response shoul not be null", response);
        assertEquals("Count should be the same", count, response.body().size());
        assertTrue("State1 should be present", response.body().get((response.body().size() - 1)).getId()
                == state3.getId());
    }

    @Test
    public void testGetState() throws IOException {
        stateService.create(state1);
        stateService.create(state2);
        stateService.create(state3);

        Response<State> response = restService.getState(state1.getId()).execute();

        assertNotNull("Response shoul not be null", response);
        assertTrue("State1 should be present", response.body().getId() == state1.getId());
    }
}
