package ru.tinkoff.edu.java.scrapper.service.updater;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
        List<Pair<Link, String>> updatedLinksList = new ArrayList<>();
        for (Link link : linkList) {
            Pair<Link, String> linkWithDesc = update(link);

            if (linkWithDesc != null) {
                updatedLinksList.add(linkWithDesc);
            }
        }

        updatedLinksList.forEach(pair -> linkRepository.save(pair.getKey()));
        notifyBot(updatedLinksList);
        return updatedLinksList.size();
    }

    @Override
    public Pair<Link, String> update(Link link) {
        UrlData urlData = parseLink(link.getLink());

        if (urlData == null) {
            return null;
        }

        Pair<Link, String> linkWithDesc = null;
        switch (urlData.getClass().getSimpleName()) {
            case "GitHubData" -> linkWithDesc = handleGitHubLink(link, (GitHubData) urlData);
            case "StackOverflowData" -> linkWithDesc = handleStackOverflowLink(link, (StackOverflowData) urlData);
        }
        return linkWithDesc;
    }

    private Pair<Link, String> handleGitHubLink(Link link, GitHubData gitHubData) {
        GitHubRepositoryResponse response =
                gitHubClient.fetchRepository(gitHubData.username(), gitHubData.repos()).block();
        if (response != null && link.getUpdatedAt() != response.updatedAt()) {
            String description = checkGithubChanges(link, response);

            link.setUpdatedAt(response.updatedAt());
            link.setGhForksCount(response.forksCount());
            link.setGhBranchesCount(response.branchesCount());
            return new ImmutablePair<>(link, description);
        }
        return null;
    }

    private String checkGithubChanges(Link link, GitHubRepositoryResponse response) {
        //sb используется с расчетом на то, что в дальнейшем будет больше проверок
        StringBuilder description = new StringBuilder();
        if (link.getGhForksCount() > response.forksCount()) {
            description.append("Количество форков уменьшилось, ");
        } else if (link.getGhForksCount() < response.forksCount()) {
            description.append("Количество форков увеличилось, ");
        }

        if (link.getGhBranchesCount() > response.branchesCount()) {
            description.append("Количество веток уменьшилось");
        } else if (link.getGhBranchesCount() < response.branchesCount()) {
            description.append("Количество веток увеличилось");
        }
        return description.toString();
    }

    private Pair<Link, String> handleStackOverflowLink(Link link, StackOverflowData stackOverflowData) {
        StackOverflowQuestionResponse response =
                stackOverflowClient.fetchQuestion(stackOverflowData.id()).block();
        if (response != null && link.getUpdatedAt() != response.UpdatedAt()) {
            String description = checkStackoverflowChanges(link, response);

            link.setUpdatedAt(response.UpdatedAt());
            link.setSoAnswersCount(response.answerCount());
            return new ImmutablePair<>(link, description);
        }
        return null;
    }

    private String checkStackoverflowChanges(Link link, StackOverflowQuestionResponse response) {
        //sb используется с расчетом на то, что в дальнейшем будет больше проверок
        StringBuilder description = new StringBuilder();
        if (link.getSoAnswersCount() < response.answerCount()) {
            description.append("Появились новые ответы");
        }
        return description.toString();
    }

    private UrlData parseLink(String link) {
        Handler handler = HandlerBuilder.build();
        return handler.parse(new Request(link));
    }

    private void notifyBot(List<Pair<Link, String>> linksWithDesc) {
        linksWithDesc.forEach(pair -> {
            Link link = pair.getKey();
            String desc = pair.getValue();
            LinkUpdateRequest request = new LinkUpdateRequest(
                    link.getId(),
                    URI.create(link.getLink()),
                    desc,
                    subscription.findChatsByLinkId(link.getId()).stream()
                            .map(Chat::getId)
                            .toList()
            );
            botClient.postUpdate(request);
        });
    }
}
