package ru.tinkoff.edu.java.scrapper.configuration;

import java.time.Duration;
import jakarta.validation.constraints.NotNull;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "scrapper", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull Scheduler scheduler,
                                @JsonProperty("update-link-interval") @NotNull Duration updateLinkInterval) {
    public record Scheduler(Duration interval) {
    }
}