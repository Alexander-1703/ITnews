package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;

@RestController
public class BotController {

    @PostMapping(value = "/updates")
    public String postUpdate(@RequestBody LinkUpdateRequest request) {
        if (request.id() < 0) {
            throw new IllegalArgumentException("Request id can`t be negative");
        }
        return "Обновление обработано";
    }

}
