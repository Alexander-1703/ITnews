package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubBranchResponse(
    @JsonProperty("name") String name
) {
}
