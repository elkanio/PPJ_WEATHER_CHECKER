package ppj.weather.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import ppj.weather.model.WeatherRecord;

@Configuration
public class ExpirationConfigurations implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ExpirationConfigurations.class);

    private final WeathersConfigurations weathersConfigurations;

    private final MongoTemplate mongoTemplate;

    public ExpirationConfigurations(WeathersConfigurations weathersConfigurations, MongoTemplate mongoTemplate) {
        this.weathersConfigurations = weathersConfigurations;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("Expiration of data is set to " + weathersConfigurations.getExpiration() + "s");

        try {
            setExpirationOnDateIndex();
        } catch(Exception ex) {
            mongoTemplate.indexOps(WeatherRecord.COLLECTION_NAME).dropIndex(WeatherRecord.DATE_NAME);

            setExpirationOnDateIndex();
        }

    }

    private void setExpirationOnDateIndex() {
        mongoTemplate.indexOps(WeatherRecord.COLLECTION_NAME)
                .ensureIndex(
                        new Index().on(WeatherRecord.DATE_NAME, Sort.Direction.ASC)
                                .expire(weathersConfigurations.getExpiration())
                                .named(WeatherRecord.DATE_NAME)
                );
    }
}
