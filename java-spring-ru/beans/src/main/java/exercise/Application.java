package exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

import exercise.daytime.Daytime;
import exercise.daytime.Day;
import exercise.daytime.Night;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;

// BEGIN

// END

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @RequestScope
    @Bean
    public Daytime getDaytime() {
        Daytime daytime;
        var time = LocalDateTime.now();

        if (time.getHour() >= 6 && time.getHour() < 22) {
            daytime = new Day();
        } else {
            daytime = new Night();
        }

        return daytime;
    }
    // END
}
