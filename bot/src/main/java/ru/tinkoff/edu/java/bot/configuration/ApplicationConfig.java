package ru.tinkoff.edu.java.bot.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test,
                                @NotNull String name,
                                @NotNull String token,
                                @NotNull Updates updates)
{
    public record Updates(Duration fixedDelay) {
    }
}