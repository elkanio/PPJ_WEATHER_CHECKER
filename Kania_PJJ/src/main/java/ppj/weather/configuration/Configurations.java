package ppj.weather.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ppj.weather.Main;
import ppj.weather.web.mOnlyGet;


@Conditional(OnlyReadConfigurations.ReadOnlyModeEnabled.class)
@org.springframework.context.annotation.Configuration
public class Configurations implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public Configurations() {
        log.info("Read only mode is enabled.");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new mOnlyGet());
    }

}
