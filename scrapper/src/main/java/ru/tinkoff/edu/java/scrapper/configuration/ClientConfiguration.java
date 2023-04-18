package ru.tinkoff.edu.java.scrapper.configuration;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.scrapper.client.BotClientImpl;
import ru.tinkoff.edu.java.scrapper.client.interfaces.BotClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClientImpl;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClientImpl;

@Configuration
public class ClientConfiguration {
    private final String githubUrl;
    private final String stackoverflowUrl;
    private final String botUrl;

    public ClientConfiguration(@Value("${client.github-api:https://api.github.com}") String githubUrl,
                               @Value("${client.stackoverflow-api:https://api.stackexchange.com/2.3}") String stackoverflowUrl,
                               @Value("${client.bot-url:http://localhost:8080}") String botUrl) {
        this.githubUrl = githubUrl;
        this.stackoverflowUrl = stackoverflowUrl;
        this.botUrl = botUrl;
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
    public BotClient botWebClient() {
        return new BotClientImpl(botUrl);
    }

    @Bean
    public Duration scheduler(ApplicationConfig appConfig) {
        return appConfig.scheduler().interval();
    }

    @Bean
    public Duration linkUpdateInterval(ApplicationConfig appConfig) {
        return appConfig.updateLinkInterval();
    }
}
