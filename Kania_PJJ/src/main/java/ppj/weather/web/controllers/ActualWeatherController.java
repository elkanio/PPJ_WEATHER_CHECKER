package ppj.weather.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ppj.weather.model.State;
import ppj.weather.model.LatestWeather;
import ppj.weather.services.StateService;
import ppj.weather.services.WeatherRecordService;
import ppj.weather.web.BasicRoutes;

import java.util.List;

@Controller
public class ActualWeatherController {

    private StateService stateService;

    private WeatherRecordService weatherRecordService;

    public ActualWeatherController(StateService stateService, WeatherRecordService weatherRecordService) {
        this.stateService = stateService;
        this.weatherRecordService = weatherRecordService;
    }

    @GetMapping({BasicRoutes.ROOT, BasicRoutes.INDEX, BasicRoutes.LATEST_WEATHER})
    public String index(Model model) {
        List<State> states = stateService.getAllStates();
        model.addAttribute("states", states);

        return "index";
    }

    @GetMapping(BasicRoutes.LATEST_WEATHER_WITH_ID)
    public String dataForState(@PathVariable("id") long id, Model model) {
        List<State> states = stateService.getAllStates();
        model.addAttribute("states", states);

        List<LatestWeather> latestWeatherRecords =
                weatherRecordService.getCitiesWithLatestWeatherForState((int)id);
        model.addAttribute("weatherRecords", latestWeatherRecords);

        return "latest-weather";
    }
}
