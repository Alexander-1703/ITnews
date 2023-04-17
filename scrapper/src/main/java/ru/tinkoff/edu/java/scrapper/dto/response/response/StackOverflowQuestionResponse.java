package ru.tinkoff.edu.java.scrapper.dto.response.response;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionResponse(
        @JsonProperty("title") String title,
        @JsonProperty("question_id") Integer questionId,
        @JsonProperty("last_edit_date") OffsetDateTime UpdatedAt
) {
}
