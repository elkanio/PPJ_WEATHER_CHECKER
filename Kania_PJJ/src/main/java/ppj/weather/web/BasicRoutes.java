package ppj.weather.web;

public interface BasicRoutes {
    String ROOT = "/";
    String INDEX = "/index";
    String LATEST_WEATHER = "/weather_now/";
    String LATEST_WEATHER_WITH_ID = LATEST_WEATHER+"/{id}";

}
