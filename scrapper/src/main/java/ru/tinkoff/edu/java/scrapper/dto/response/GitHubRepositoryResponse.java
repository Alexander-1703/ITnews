package ru.tinkoff.edu.java.scrapper.dto.response;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubRepositoryResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("updated_at") OffsetDateTime updatedAt,
        @JsonProperty("forks_count") int forksCount,
        @JsonProperty("branches_count") int branchesCount
) {
}
