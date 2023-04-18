package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

public class GitHubClientImpl implements GitHubClient {
    private static final String GITHUB_URI = "/repos/{owner}/{repo}";
    private final String url;
    private WebClient githubWebClient;

    public GitHubClientImpl(String url) {
        this.url = url;
    }

    @PostConstruct
    public void buildGithubWebClient() {
        githubWebClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        return githubWebClient.get()
                .uri(GITHUB_URI, owner, repo)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class);
    }
}
