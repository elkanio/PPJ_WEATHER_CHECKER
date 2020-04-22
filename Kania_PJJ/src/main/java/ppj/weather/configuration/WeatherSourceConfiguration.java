package ppj.weather.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("weather.source")
public class WeatherSourceConfiguration {

    private String apiKey = "feb37adf2589c864c7f2bd590c9a8086";

    /**
     * Interval in seconds.
     */
    private int updateRate = 60;

    /**
     * Per minute.
     */
    private int maxCalls = 60;

    private String weatherUrl;

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int update) {
        if(update < 1) {
            return;
        }

        if((update / 60) > maxCalls) {
            update = 1;
        }

        this.updateRate = update;
    }

    public int getMaxCalls() {
        return maxCalls;
    }

    public void setMaxCalls(int maxCalls) {
        if(maxCalls <= 0) {
            return;
        }

        this.maxCalls = maxCalls;
    }
}
