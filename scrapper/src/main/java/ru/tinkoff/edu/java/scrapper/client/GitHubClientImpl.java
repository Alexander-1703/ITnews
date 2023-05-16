package ru.tinkoff.edu.java.scrapper.client;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.interfaces.GitHubClient;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.GithubBranchResponse;

public class GitHubClientImpl implements GitHubClient {
    private static final String GITHUB_URI = "/repos/{owner}/{repo}";
    private final String url;
    private final String githubAccessToken;
    private WebClient githubWebClient;

    public GitHubClientImpl(String url, String githubAccessToken) {
        this.url = url;
        this.githubAccessToken = githubAccessToken;
    }

    @PostConstruct
    public void buildGithubWebClient() {
        githubWebClient = WebClient.builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.AUTHORIZATION, githubAccessToken)
            .build();
    }

    @Override
    public Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        return githubWebClient.get()
            .uri(GITHUB_URI, owner, repo)
            .retrieve()
            .bodyToMono(GitHubRepositoryResponse.class)
            .zipWith(getBranches(owner, repo))
            .map(tuple -> new GitHubRepositoryResponse(
                tuple.getT1().id(),
                tuple.getT1().fullName(),
                tuple.getT1().updatedAt(),
                tuple.getT1().forksCount(),
                tuple.getT2().size()
            ));
    }

    private Mono<List<String>> getBranches(String owner, String repo) {
        return githubWebClient.get()
            .uri(GITHUB_URI + "/branches", owner, repo)
            .retrieve()
            .bodyToFlux(GithubBranchResponse.class)
            .map(GithubBranchResponse::name)
            .collectList();
    }
}
