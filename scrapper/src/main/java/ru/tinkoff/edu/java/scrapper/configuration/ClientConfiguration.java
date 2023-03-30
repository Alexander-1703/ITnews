package ru.tinkoff.edu.java.scrapper.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClientImpl;

@Configuration
public class ClientConfiguration {
    private final String githubUrl;
    private final String stackoverflowUrl;

    public ClientConfiguration(@Value("${client.github-api:https://api.github.com}") String githubUrl,
                               @Value("${client.stackoverflow-api:https://api.stackexchange.com/2.3}") String stackoverflowUrl) {
        this.githubUrl = githubUrl;
        this.stackoverflowUrl = stackoverflowUrl;
    }

    @Bean
    public GitHubClient githubWebClient() {
        return new GitHubClientImpl(githubUrl);
    }

    @Bean
    public StackOverflowClient stackoverflowWebClient() {
        return new StackOverflowClientImpl(stackoverflowUrl);
    }

    @Bean
    public Duration scheduler(ApplicationConfig appConfig) {
        return appConfig.scheduler().interval();
    }
}
