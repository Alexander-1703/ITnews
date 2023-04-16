package ru.tinkoff.edu.java.scrapper.service.updater;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.linkparser.HandlerBuilder;
import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.response.response.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.LinkUpdater;

@Service
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {
    private final LinkRepository linkRepository;

    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Value("#{@linkUpdateInterval}")
    Duration interval;

    @Override
    public int update() {
        List<Link> linkList = linkRepository.findNotUpdated(interval);
        for (Link link : linkList) {
            UrlData urlData = parseLink(link.getLink());

            if (urlData == null) {
                continue;
            }

            switch (urlData.getClass().getSimpleName()) {
                case "GitHubData" -> {
                    GitHubData gitHubData = (GitHubData) urlData;

                }
                case "StackOverflowData" -> {
                    StackOverflowData stackOverflowData = (StackOverflowData) urlData;
                    StackOverflowQuestionResponse response =
                            stackOverflowClient.fetchQuestion(stackOverflowData.id()).block();
                }
            }
        }
        return 0;
    }

    private GitHubData handleGitHubLink(GitHubData gitHubData) {
        GitHubRepositoryResponse response =
                gitHubClient.fetchRepository(gitHubData.username(), gitHubData.repos()).block();
        if (response != null  )
    }

    private UrlData parseLink(String link) {
        Handler handler = HandlerBuilder.build();
        return handler.parse(new Request(link));
    }
}
