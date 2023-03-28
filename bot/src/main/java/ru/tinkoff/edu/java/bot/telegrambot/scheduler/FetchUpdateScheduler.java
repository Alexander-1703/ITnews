package ru.tinkoff.edu.java.bot.telegrambot.scheduler;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.telegrambot.TelegramBotImpl;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchUpdateScheduler {
    private final TelegramBotImpl telegramBot;

    @Scheduled(fixedDelayString = "#{updatesScheduler}")
    public void update() {
        log.info("Fetching bot updates...");
        telegramBot.fetchUpdates();
    }
}
