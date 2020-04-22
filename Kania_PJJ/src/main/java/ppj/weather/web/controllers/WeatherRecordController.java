package ppj.weather.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppj.weather.model.WeatherRecord;
import ppj.weather.services.WeatherRecordService;
import ppj.weather.web.Api;

import java.util.List;
import java.util.Optional;

@RestController
public class WeatherRecordController {

    private WeatherRecordService weatherRecordService;

    @Autowired
    public WeatherRecordController(WeatherRecordService weatherRecordService) {
        this.weatherRecordService = weatherRecordService;
    }

    @RequestMapping(value = Api.WEATHER_RECORDS_PATH, method = RequestMethod.GET)
    public ResponseEntity<List<WeatherRecord>> getWeatherRecords() {
        List<WeatherRecord> weatherRecords = weatherRecordService.getAll();

        return new ResponseEntity<>(weatherRecords, HttpStatus.OK);
    }

    @RequestMapping(value = Api.WEATHER_RECORDS_PATH, method = RequestMethod.POST)
    public ResponseEntity<WeatherRecord> createWeatherRecord(@RequestBody WeatherRecord weatherRecord) {
        if(weatherRecord.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            weatherRecordService.create(weatherRecord);

            return new ResponseEntity<>(weatherRecord, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = Api.WEATHER_RECORD_PATH, method = RequestMethod.PUT)
    public ResponseEntity<WeatherRecord> updateWeatherRecord(@RequestBody WeatherRecord weatherRecord) {
        Optional<WeatherRecord> result = weatherRecordService.get(weatherRecord.getId());

        if(result.isPresent()) {
            WeatherRecord updated = weatherRecordService.save(weatherRecord);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = Api.WEATHER_RECORD_PATH, method = RequestMethod.GET)
    public ResponseEntity<WeatherRecord> getWeatherRecord(@PathVariable("id") String id) {
        Optional<WeatherRecord> result = weatherRecordService.get(id);

        if(result.isPresent() == false) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = Api.WEATHER_RECORD_PATH, method = RequestMethod.DELETE)
    public ResponseEntity deleteWeatherRecord(@PathVariable("id") String id) {
        Optional<WeatherRecord> result = weatherRecordService.get(id);

        if(result.isPresent()) {
            weatherRecordService.delete(result.get());

            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
