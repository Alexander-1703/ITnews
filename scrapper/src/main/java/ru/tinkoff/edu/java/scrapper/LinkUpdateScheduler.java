package ru.tinkoff.edu.java.scrapper;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class LinkUpdateScheduler {

    @Scheduled(fixedDelayString = "#{@scheduler}")
    public void update() {
        log.info("Updating links...");

    }
}
