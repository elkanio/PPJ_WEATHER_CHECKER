package ppj.weather.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;

@Document(collection = WeatherRecord.COLLECTION_NAME)
public class WeatherRecord {

    public static final String COLLECTION_NAME = "weatherRecords";
    public static final String HUMIDITY_NAME = "humidity";
    public static final String TEMPERATURE_NAME = "temperature";
    public static final String PRECIPITATION_NAME = "precipitation";
    public static final String DATE_NAME = "date";
    public static final String CITY_ID_NAME = "cityId";
    public static final String ID_NAME = "_id";

    @Id
    @GeneratedValue
    private String id;

    @JsonIgnore
    private City city;

    private int cityId;

    private Date date;

    private double temperature;

    private double humidity;

    private double precipitation;

    public WeatherRecord() {
    }

    public WeatherRecord(String id, int cityId, Date date,
                         double temperature, double humidity, double precipitation) {
        this.id = id;
        this.cityId = cityId;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    public WeatherRecord(int cityId, double temperature, double humidity,
                         double precipitation) {
        this.cityId = cityId;
        this.date = new Date();
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }

    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipitation() {
        return precipitation;
    }
    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    @Override
    public String toString() {
        return "WeatherRecord{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", precipitation=" + precipitation +
                '}';
    }
}
