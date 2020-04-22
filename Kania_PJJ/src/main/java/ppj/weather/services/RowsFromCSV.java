package ppj.weather.services;

import java.util.Date;

public class RowsFromCSV {
    private Date date;
    private double temperature;
    private double humidity;
    private double precipitation;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }
    public double getHumidity() {
        return humidity;
    }
    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "RowsFromCSV{" +
                "date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitation=" + precipitation +
                '}';
    }

}
