package ppj.weather.model;

import java.util.Date;

public class WeatherAverages extends WeatherRecord {

    private Intervals intervals = Intervals.DAY;

    public WeatherAverages() {
    }

    public WeatherAverages(int cityId, double temperature, double humidity, double precipitation) {
        super(cityId, temperature, humidity, precipitation);
    }

    @Override
    @Deprecated
    public void setDate(Date date) {
        super.setDate(date);
    }

    @Override
    @Deprecated
    public String getId() {
        return super.getId();
    }

    @Override
    @Deprecated
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    @Deprecated
    public Date getDate() {
        return super.getDate();
    }

    public Intervals getIntervals() {
        return intervals;
    }

    public void setIntervals(Intervals intervals) {
        this.intervals = intervals;
    }

    @Override
    public String toString() {
        return "WeatherRecord{" +
                ", cityId=" + super.getCityId() +
                ", temperature=" + super.getTemperature() +
                ", humidity=" + super.getHumidity() +
                ", precipitation=" + super.getPrecipitation() +
                '}';
    }

}
