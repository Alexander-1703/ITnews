package ru.tinkoff.edu.java.scrapper.service.updater;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.linkparser.HandlerBuilder;
import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.scrapper.client.interfaces.BotClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;

@Service
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final LinkChatRepository subscription;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Value("#{@linkUpdateInterval}")
    Duration interval;

    @Override
    public int update() {
        List<Link> linkList = linkRepository.findNotUpdated(interval);
        List<Link> updatedLinksList = new ArrayList<>();
        for (Link link : linkList) {
            UrlData urlData = parseLink(link.getLink());

            if (urlData == null) {
                continue;
            }

            Link updatedLink = null;
            switch (urlData.getClass().getSimpleName()) {
                case "GitHubData" -> updatedLink = handleGitHubLink(link, (GitHubData) urlData);
                case "StackOverflowData" -> updatedLink = handleStackOverflowLink(link, (StackOverflowData) urlData);
            }

            if (updatedLink != null) {
                updatedLinksList.add(updatedLink);
            }
        }

        updatedLinksList.forEach(linkRepository::save);
        notifyBot(updatedLinksList);
        return updatedLinksList.size();
    }

    private Link handleGitHubLink(Link link, GitHubData gitHubData) {
        GitHubRepositoryResponse response =
                gitHubClient.fetchRepository(gitHubData.username(), gitHubData.repos()).block();
        if (response != null) {
            link.setUpdatedAt(response.updatedAt());
            return link;
        }
        return null;
    }

    private Link handleStackOverflowLink(Link link, StackOverflowData stackOverflowData) {
        StackOverflowQuestionResponse response =
                stackOverflowClient.fetchQuestion(stackOverflowData.id()).block();
        if (response != null) {
            link.setUpdatedAt(response.UpdatedAt());
            return link;
        }
        return null;
    }

    private UrlData parseLink(String link) {
        Handler handler = HandlerBuilder.build();
        return handler.parse(new Request(link));
    }

    private void notifyBot(List<Link> linkList) {
        linkList.forEach(link -> {
            LinkUpdateRequest request = new LinkUpdateRequest(
                    link.getId(),
                    URI.create(link.getLink()),
                    "что-то обновилось",
                    subscription.findChatsByLinkId(link.getId()).stream()
                            .map(Chat::getId)
                            .toList()
            );
            botClient.postUpdate(request);
        });
    }
}
