package ru.tinkoff.edu.java.scrapper.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionsListResponse(
        @JsonProperty("items")
        List<StackOverflowQuestionResponse> items
) {
}
