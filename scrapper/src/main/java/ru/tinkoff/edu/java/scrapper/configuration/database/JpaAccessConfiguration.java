package ru.tinkoff.edu.java.scrapper.configuration.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaSubscriptionService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;
import ru.tinkoff.edu.java.scrapper.service.producer.ScrapperProducer;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scrapper", name = "accessType", havingValue = "jpa", matchIfMissing = true)
public class JpaAccessConfiguration {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ScrapperProducer scrapperProducer;
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Bean
    public JpaSubscriptionService subscriptionService() {
        return new JpaSubscriptionService(linkRepository, chatRepository);
    }

    @Bean
    public JpaLinkUpdater linkUpdater() {
        return new JpaLinkUpdater(
            linkRepository,
            subscriptionService(),
            gitHubClient,
            stackOverflowClient,
            scrapperProducer
        );
    }

    @Bean
    public JpaLinkService linkService() {
        return new JpaLinkService(linkRepository, subscriptionService(), linkUpdater());
    }

    @Bean
    public JpaTgChatService chatService() {
        return new JpaTgChatService(chatRepository);
    }
}
