package ru.tinkoff.edu.java.scrapper.service.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "scrapper", name = "use-queue", havingValue = "true")
public class ScrapperQueueProducer implements ScrapperProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationConfig config;

    @Override
    public boolean postUpdate(LinkUpdateRequest request) {
        try {
            rabbitTemplate.convertAndSend(
                config.rabbitMQ().exchange(),
                config.rabbitMQ().routingKey(),
                request
            );
        } catch (AmqpException e) {
            log.error("Error sending message to queue: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
