package ru.tinkoff.edu.java.scrapper.configuration.database;

import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqTgChatService;
import ru.tinkoff.edu.java.scrapper.service.producer.ScrapperProducer;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scrapper", name = "accessType", havingValue = "jooq")
public class JooqAccessConfiguration {
    private final DSLContext context;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ScrapperProducer scrapperProducer;

    @Bean
    public JooqLinkRepository linkRepository() {
        return new JooqLinkRepository(context);
    }

    @Bean
    public JooqChatRepository chatRepository() {
        return new JooqChatRepository(context);
    }

    @Bean
    public JooqLinkChatRepository linkChatRepository() {
        return new JooqLinkChatRepository(context);
    }

    @Bean
    public JooqLinkUpdater linkUpdater() {
        return new JooqLinkUpdater(
            linkRepository(),
            linkChatRepository(),
            gitHubClient,
            stackOverflowClient,
            scrapperProducer
        );
    }

    @Bean
    public JooqLinkService linkService() {
        return new JooqLinkService(linkRepository(), linkChatRepository(), linkUpdater());
    }

    @Bean
    public JooqTgChatService chatService() {
        return new JooqTgChatService(chatRepository());
    }
}
