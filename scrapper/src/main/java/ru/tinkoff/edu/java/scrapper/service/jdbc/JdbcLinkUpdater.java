package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.linkparser.HandlerBuilder;
import ru.tinkoff.edu.java.linkparser.Request.Request;
import ru.tinkoff.edu.java.linkparser.dtos.GitHubData;
import ru.tinkoff.edu.java.linkparser.dtos.StackOverflowData;
import ru.tinkoff.edu.java.linkparser.dtos.UrlData;
import ru.tinkoff.edu.java.linkparser.handlers.Handler;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.interfaces.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.UpdatedLink;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.producer.ScrapperProducer;

@RequiredArgsConstructor
@Slf4j
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository linkRepository;
    private final JdbcLinkChatRepository subscription;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ScrapperProducer scrapperProducer;

    @Value("#{@linkUpdateInterval}")
    private Duration interval;

    @Override
    public int update() {
        List<Link> linkList = linkRepository.findNotUpdated(interval);
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
        return handleLink(urlData, link);
    }

    private UpdatedLink handleLink(UrlData urlData, Link link) {
        switch (urlData.getClass().getSimpleName()) {
            case "GitHubData" -> {
                return handleGitHubLink(link, (GitHubData) urlData);
            }
            case "StackOverflowData" -> {
                return handleStackOverflowLink(link, (StackOverflowData) urlData);
            }
            default -> log.warn("Unexpected class to handle");
        }
        return null;
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

    private void appendCountAndChanges(StringBuilder description, String title, int count, String changes) {
        description
            .append(title)
            .append(": ")
            .append(count)
            .append(" ")
            .append(changes)
            .append("\n");
    }

    private String checkGithubChanges(Link link, GitHubRepositoryResponse response) {
        StringBuilder description = new StringBuilder();

        String forksChanges = link.getGhForksCount() == null ? "" :
            showChangesBetweenInts(link.getGhForksCount(), response.forksCount());
        String branchChanges = link.getGhBranchesCount() == null ? "" :
            showChangesBetweenInts(link.getGhBranchesCount(), response.branchesCount());

        appendCountAndChanges(description, "Количество форков", response.forksCount(), forksChanges);
        appendCountAndChanges(description, "Количество веток", response.branchesCount(), branchChanges);
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

        String answerChanges = link.getSoAnswersCount() == null ? "" :
            showChangesBetweenInts(link.getSoAnswersCount(), response.answerCount());

        appendCountAndChanges(description, "Количество ответов", response.answerCount(), answerChanges);
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
                subscription.findChatsByLinkId(link.getId()).stream()
                    .map(Chat::getId)
                    .toList()
            );
            if (!scrapperProducer.postUpdate(request)) {
                log.warn("Update failed!");
            }
        });
    }
}
