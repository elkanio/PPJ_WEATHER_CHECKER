package ppj.weather.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppj.weather.model.State;
import ppj.weather.services.StateService;
import ppj.weather.web.Api;

import java.util.List;
import java.util.Optional;

@RestController
public class StateController {

    private StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @RequestMapping(value = Api.STATES_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<State>> getStates() {
        List<State> states = stateService.getAllStates();

        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    @RequestMapping(value = Api.STATES_PATH, method = RequestMethod.POST)
    public ResponseEntity<State> createState(@RequestBody State state) {
        if(stateService.exists(state.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            stateService.create(state);

            return new ResponseEntity<>(state, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = Api.STATE_PATH, method = RequestMethod.PUT)
    public ResponseEntity<State> updateState(@RequestBody State state) {
        Optional<State> result = stateService.get(state.getId());

        if(result.isPresent()) {
            State updated = stateService.save(state);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = Api.STATE_PATH, method = RequestMethod.GET)
    public ResponseEntity<State> getState(@PathVariable("id") int id) {
        Optional<State> result = stateService.get(id);

        if(result.isPresent() == false) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = Api.STATE_PATH, method = RequestMethod.DELETE)
    public ResponseEntity deleteState(@PathVariable("id") int id) {
        Optional<State> result = stateService.get(id);

        if(result.isPresent()) {
            stateService.delete(result.get());

            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
