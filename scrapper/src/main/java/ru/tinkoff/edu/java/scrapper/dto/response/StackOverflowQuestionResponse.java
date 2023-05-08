package ru.tinkoff.edu.java.scrapper.dto.response;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionResponse(
    @JsonProperty("title") String title,
    @JsonProperty("question_id") Integer questionId,
    @JsonProperty("last_activity_date") OffsetDateTime updatedAt,
    @JsonProperty("answer_count") Integer answerCount
) {
}
