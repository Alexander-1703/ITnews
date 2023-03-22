package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

@Component
public class GitHubClientImpl implements GitHubClient {
    private final WebClient gitHubWebClient;

    @Autowired
    public GitHubClientImpl(@Qualifier("githubWebClient") WebClient gitHubWebClient) {
        this.gitHubWebClient = gitHubWebClient;
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        return gitHubWebClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class);
    }
}
