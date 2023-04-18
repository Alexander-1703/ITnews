package ru.tinkoff.edu.java.scrapper.client.interfaces;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

public interface GitHubClient {
    Mono<GitHubRepositoryResponse> fetchRepository(String owner, String repo);
}
