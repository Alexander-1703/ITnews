package ru.tinkoff.edu.java.scrapper.service.jpa;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.linkparser.HandlerBuilder;
import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.scrapper.client.interfaces.BotClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.UpdatedLink;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

@Service
@Slf4j
@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private final JpaLinkRepository linkRepository;
    private final JpaLinkService linkService;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Value("#{@linkUpdateInterval}")
    Duration interval;

    @Override
    public int update() {
        OffsetDateTime dateTime = OffsetDateTime.now().minus(interval);
        List<Link> linkList = linkRepository.findAllByUpdatedAtBefore(dateTime);
        List<UpdatedLink> updatedLinksList = new ArrayList<>();
        for (Link link : linkList) {
            UpdatedLink updatedLink = update(link);

            if (updatedLink != null) {
                updatedLinksList.add(updatedLink);
            }
        }

        updatedLinksList.forEach(updatedLink -> linkRepository.save(updatedLink.link()));
        notifyBot(updatedLinksList);
        return updatedLinksList.size();
    }

    @Override
    public UpdatedLink update(Link link) {
        UrlData urlData = parseLink(link.getLink());
        if (urlData == null) {
            return null;
        }

        UpdatedLink updatedLink = null;
        switch (urlData.getClass().getSimpleName()) {
            case "GitHubData" -> updatedLink = handleGitHubLink(link, (GitHubData) urlData);
            case "StackOverflowData" -> updatedLink = handleStackOverflowLink(link, (StackOverflowData) urlData);
        }
        return updatedLink;
    }

    private UpdatedLink handleGitHubLink(Link link, GitHubData gitHubData) {
        GitHubRepositoryResponse response =
                gitHubClient.fetchRepository(gitHubData.username(), gitHubData.repos()).block();
        if (response != null && !link.getUpdatedAt().equals(response.updatedAt())) {
            String description = checkGithubChanges(link, response);

            link.setUpdatedAt(response.updatedAt());
            link.setGhForksCount(response.forksCount());
            link.setGhBranchesCount(response.branchesCount());

            return new UpdatedLink(link, description);
        }
        return null;
    }

    private String checkGithubChanges(Link link, GitHubRepositoryResponse response) {
        StringBuilder description = new StringBuilder();
        description.append("Количество форков: ").append(response.forksCount()).append(" ");
        String forksChanges = link.getGhForksCount() == null ? "" :
                showChangesBetweenInts(link.getGhForksCount(), response.forksCount());
        description.append(forksChanges).append("\n");

        description.append("Количество веток: ").append(response.branchesCount()).append(" ");
        String branchChanges = link.getGhBranchesCount() == null ? "" :
                showChangesBetweenInts(link.getGhBranchesCount(), response.branchesCount());
        description.append(branchChanges).append("\n");

        return description.toString();
    }

    private String showChangesBetweenInts(int a, int b) {
        if (a < b) {
            return String.format("(+%s)", b - a);
        } else if (a > b) {
            return String.format("(%s)", b - a);
        }
        return "";
    }

    private UpdatedLink handleStackOverflowLink(Link link, StackOverflowData stackOverflowData) {
        StackOverflowQuestionResponse response =
                stackOverflowClient.fetchQuestion(stackOverflowData.id()).block();
        if (response != null && !link.getUpdatedAt().equals(response.updatedAt())) {
            String description = checkStackoverflowChanges(link, response);

            link.setUpdatedAt(response.updatedAt());
            link.setSoAnswersCount(response.answerCount());
            return new UpdatedLink(link, description);
        }
        return null;
    }

    private String checkStackoverflowChanges(Link link, StackOverflowQuestionResponse response) {
        StringBuilder description = new StringBuilder();
        description.append("Количество ответов: ").append(response.answerCount()).append(" ");
        String answerChanges = link.getSoAnswersCount() == null ? "" :
                showChangesBetweenInts(link.getSoAnswersCount(), response.answerCount());
        description.append(answerChanges).append("\n");

        return description.toString();
    }

    private UrlData parseLink(String link) {
        Handler handler = HandlerBuilder.build();
        return handler.parse(new Request(link));
    }

    private void notifyBot(List<UpdatedLink> updatedLinkList) {
        updatedLinkList.forEach(updatedLink -> {
            Link link = updatedLink.link();
            LinkUpdateRequest request = new LinkUpdateRequest(
                    link.getId(),
                    URI.create(link.getLink()),
                    updatedLink.description(),
                    linkService.findChatsByLinkId(link.getId()).stream()
                            .map(Chat::getId)
                            .toList()
            );
            botClient.postUpdate(request);
        });
    }
}
