package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.telegrambot.TelegramBotImpl;

@RestController
@RequiredArgsConstructor
public class BotController {
    private final TelegramBotImpl bot;

    @PostMapping(value = "/updates")
    @ResponseStatus(HttpStatus.OK)
    public void postUpdate(@RequestBody LinkUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is empty");
        }
        bot.sendUpdate(request);
    }
}
