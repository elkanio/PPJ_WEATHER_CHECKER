package ppj.weather.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppj.weather.model.StatisticExt;
import ppj.weather.model.LatestWeather;
import ppj.weather.model.WeatherAverages;
import ppj.weather.model.Intervals;
import ppj.weather.services.WeatherRecordService;
import ppj.weather.web.Api;

@RestController
public class WeatherStatController {

    private WeatherRecordService weatherRecordService;

    @Autowired
    public WeatherStatController(WeatherRecordService weatherRecordService) {
        this.weatherRecordService = weatherRecordService;
    }

    @RequestMapping(value = Api.LATEST_CITY_WEATHER_PATH, method = RequestMethod.GET)
    public ResponseEntity<LatestWeather> getLatestWeatherForCity(@PathVariable("id_city") int idCity) {
        LatestWeather record = weatherRecordService.getLatestWeatherRecordForCity(idCity);

        if(record == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(record, HttpStatus.OK);
        }
    }

    @RequestMapping(value = Api.AVERAGE_CITY_WEATHER_PATH, method = RequestMethod.GET)
    public ResponseEntity<WeatherAverages> getCityLatestAverages(@PathVariable("id_city") int idCity,
                                                                 @RequestParam(value = "interval", required = false) String _interval) {

        Intervals interval =
                StatisticExt.getWeatherStatisticsIntervalByInput(_interval);

        WeatherAverages result = weatherRecordService.getAverageWeatherForCity(idCity, interval);

        if(result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
