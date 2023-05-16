package ru.tinkoff.edu.java.scrapper.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "scrapper", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull Scheduler scheduler,
                                @JsonProperty("update-link-interval") @NotNull Duration updateLinkInterval,
                                @NotNull AccessType accessType,
                                boolean useQueue,
                                RabbitMQ rabbitMQ) {
    public record Scheduler(Duration interval) {
    }

    public enum AccessType {
        JDBC,
        JPA,
        JOOQ;
    }

    public record RabbitMQ(@NotBlank String exchange,
                           @NotBlank String queue,
                           @NotBlank String routingKey) {
    }
}
