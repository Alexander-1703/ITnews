package ru.tinkoff.edu.java.bot.service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.telegrambot.TelegramBotImpl;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "scrapper", name = "use-queue", havingValue = "true")
@RabbitListener(queues = "${bot.rabbitmq.queue}")
public class ScrapperQueueListener {
    private final TelegramBotImpl bot;

    @RabbitHandler
    public void receiver(LinkUpdateRequest update) {
        log.info("Received update by rabbitmq: {}", update);
        bot.sendUpdate(update);
    }
}
