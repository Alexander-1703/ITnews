package ru.tinkoff.edu.java.scrapper.client.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Value("${github.api}")
    String githubBaseUrl;
    @Value("${stackoverflow.api}")
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
}
