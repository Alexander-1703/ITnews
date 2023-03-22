package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;

public interface StackOverflowClient {
    Mono<StackOverflowQuestionResponse> fetchQuestion(int questionId);
}
