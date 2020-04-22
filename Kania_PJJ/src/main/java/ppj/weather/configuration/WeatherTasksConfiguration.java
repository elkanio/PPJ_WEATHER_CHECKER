package ppj.weather.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

//@Conditional(OnlyReadConfigurations.ReadOnlyModeDisabled.class)
@Configuration
public class WeatherTasksConfiguration {

    @Bean
    ThreadPoolTaskScheduler weatherThreadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

}
