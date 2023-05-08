package ru.tinkoff.edu.java.bot.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String name,
                                @NotNull String token,
                                @NotNull Updates updates,
                                @NotNull RabbitMQ rabbitMQ) {
    public record Updates(Duration fixedDelay) {
    }

    public record RabbitMQ(@NotBlank String exchange,
                           @NotBlank String queue,
                           @NotBlank String routingKey) {
    }
}
