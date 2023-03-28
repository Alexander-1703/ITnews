package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ru.tinkoff.edu.java.bot.telegrambot.scheduler.FetchUpdates;

@Validated
@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test,
                                @NotNull String name,
                                @NotNull String token,
                                @NotNull FetchUpdates fetchUpdates) {}