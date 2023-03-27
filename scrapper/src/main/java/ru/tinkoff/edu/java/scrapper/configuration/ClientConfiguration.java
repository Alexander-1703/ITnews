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

    public ClientConfiguration(@Value("${client.github-api}") String githubUrl,
                               @Value("${client.stackoverflow-api}") String stackoverflowUrl) {
        this.githubUrl = githubUrl;
        this.stackoverflowUrl = stackoverflowUrl;
    }

    @Bean
    public GitHubClient githubWebClient() {
        if (githubUrl != null) {
            return new GitHubClientImpl(githubUrl);
        }
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverflowClient stackoverflowWebClient() {
        if (stackoverflowUrl != null) {
            return new StackOverflowClientImpl(stackoverflowUrl);
        }
        return new StackOverflowClientImpl();
    }

    @Bean
    public Duration scheduler(ApplicationConfig appConfig) {
        return appConfig.scheduler().interval();
    }
}
