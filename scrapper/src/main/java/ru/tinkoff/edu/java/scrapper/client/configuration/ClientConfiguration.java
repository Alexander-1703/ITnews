package ru.tinkoff.edu.java.scrapper.client.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@Configuration
public class ClientConfiguration {
    @Value("${github.api:https://api.github.com}")
    String githubBaseUrl;
    @Value("${stackoverflow.api:https://api.stackexchange.com/2.2}")
    String stackoverflowBaseUrl;

    @Bean
    public WebClient githubWebClient() {
        return WebClient.builder()
                .baseUrl(githubBaseUrl)
                .build();
    }

    @Bean
    public WebClient stackoverflowWebClient() {
        return WebClient.builder()
                .baseUrl(stackoverflowBaseUrl)
                .build();
    }

    @Bean
    public Duration scheduler(ApplicationConfig appConfig) {
        return appConfig.scheduler().interval();
    }
}
