package ppj.weather.model;

import org.springframework.data.mongodb.core.mapping.Field;
import ppj.weather.model.City;
import ppj.weather.model.WeatherRecord;

public class LatestWeather extends WeatherRecord {

    public LatestWeather(City city, WeatherRecord weatherRecord) {
        super.setCity(city);
        super.setId(weatherRecord.getId());
        super.setDate(weatherRecord.getDate());
        super.setId(weatherRecord.getId());
        super.setCityId(weatherRecord.getCityId());
        super.setHumidity(weatherRecord.getHumidity());
        super.setPrecipitation(weatherRecord.getPrecipitation());
        super.setTemperature(weatherRecord.getTemperature());
    }

    public LatestWeather() {
    }
}
