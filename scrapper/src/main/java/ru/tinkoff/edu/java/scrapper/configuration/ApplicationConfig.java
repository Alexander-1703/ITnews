package ru.tinkoff.edu.java.scrapper.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "scrapper", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @NotNull Scheduler scheduler) {
    public record Scheduler(Duration interval) {
    }
}