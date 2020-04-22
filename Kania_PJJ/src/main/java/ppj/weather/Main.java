package ppj.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@EntityScan("ppj.weather.testy")
public class Main {

    public static void main(String[] args) throws Exception {

        SpringApplication app = new SpringApplication(Main.class);
        
        ApplicationContext ctx = app.run(args);

        System.out.println("RUNNING");

    }
}
