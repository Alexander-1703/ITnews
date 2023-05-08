package ru.tinkoff.edu.java.bot.configuration;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerUpdatesConfig {
    @Bean
    public Duration updatesScheduler(ApplicationConfig appConfig) {
        return appConfig.updates().fixedDelay();
    }
}
