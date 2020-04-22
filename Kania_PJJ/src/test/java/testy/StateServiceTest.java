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
import ppj.weather.model.State;
import ppj.weather.services.StateService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@ActiveProfiles({"test"})
@SpringBootTest
public class StateServiceTest {

    @Autowired
    private StateService stateService;

    private State state1 = new State( "UK");
    private State state2 = new State( "Czech Republic");
    private State state3 = new State( "Germany");
    private State state4 = new State( "Swiss");

    @Before
    public void init() {
        stateService.deleteStates();
    }

    @Test
    public void testCreateRetrieve() {
        stateService.create(state1);

        List<State> states1 = stateService.getAllStates();

        System.out.println(states1);

        assertEquals("One state should have been created and retrieved", 1, states1.size());

        State retrieved1 = states1.get(0);
        assertEquals("Inserted state should match retrieved", state1.toString(), retrieved1.toString());

        stateService.create(state2);
        stateService.create(state3);
        stateService.create(state4);

        List<State> states2 = stateService.getAllStates();

        assertEquals("Should be four retrieved states.", 4, states2.size());
    }

    @Test
    public void testDeleteAll()
    {
        stateService.create(state2);
        stateService.create(state3);
        stateService.create(state4);

        assertTrue("State should exist.", stateService.exists(state3.getId()));
        assertFalse("State should not exist.", stateService.exists(6666));
    }

    @Test
    public void testGet() {
        stateService.create(state2);
        stateService.create(state3);
        stateService.create(state4);

        Optional<State> result = stateService.get(state3.getId());

        assertTrue("State should be returned.", result.isPresent());
        assertTrue("Should be same", result.get().getId() == state3.getId());
    }

    @Test
    public void testSave() {
        stateService.create(state1);

        state1.setName("tested");
        stateService.save(state1);

        Optional<State> result = stateService.get(state1.getId());

        assertTrue("Should be present", result.isPresent());
        assertTrue("Should be updated", result.get().getName().equals("tested"));
    }

    @Test
    public void testDelete() {
        stateService.create(state2);
        stateService.create(state3);
        stateService.create(state4);

        stateService.delete(state3.getId());

        Optional<State> result = stateService.get(state3.getId());

        assertFalse("Should not be present", result.isPresent());
    }

    @Test
    public void testGetStateCount() {
        stateService.create(state2);
        stateService.create(state3);
        stateService.create(state4);

        int result = stateService.getStateCount();

        assertTrue("Should be equals 3", result == 3);
    }

}
