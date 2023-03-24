package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String STACKOVERFLOW_URI = "/questions/{id}";
    private final WebClient stackoverflowWebClient;

    @Autowired
    public StackOverflowClientImpl(@Qualifier("stackoverflowWebClient") WebClient stackoverflowWebClient) {
        this.stackoverflowWebClient = stackoverflowWebClient;
    }

    @Override
    public Mono<StackOverflowQuestionResponse> fetchQuestion(int questionId) {
        return stackoverflowWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(STACKOVERFLOW_URI)
                        .queryParam("site", "stackoverflow")
                        .build(questionId))
                .retrieve()
                .bodyToMono(StackOverflowQuestionResponse.class);

    }
}
