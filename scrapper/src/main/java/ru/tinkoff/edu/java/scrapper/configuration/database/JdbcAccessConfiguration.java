package ru.tinkoff.edu.java.scrapper.configuration.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;
import ru.tinkoff.edu.java.scrapper.service.producer.ScrapperProducer;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scrapper", name = "accessType", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    private final JdbcTemplate jdbcTemplate;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ScrapperProducer scrapperProducer;

    @Bean
    public JdbcLinkRepository linkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public JdbcChatRepository chatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkChatRepository linkChatRepository() {
        return new JdbcLinkChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkUpdater linkUpdater() {
        return new JdbcLinkUpdater(linkRepository(), linkChatRepository(), gitHubClient, stackOverflowClient, scrapperProducer);
    }

    @Bean
    public JdbcLinkService linkService() {
        return new JdbcLinkService(linkRepository(), linkChatRepository(), linkUpdater());
    }

    @Bean
    public JdbcTgChatService chatService() {
        return new JdbcTgChatService(chatRepository());
    }
}
