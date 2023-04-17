package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ChatRepository;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/tg-chat")
public class TgChatController {
    private final ChatRepository chatRepository;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable @PositiveOrZero Long id) {
        chatRepository.add(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable @PositiveOrZero Long id) {
        chatRepository.remove(id);
    }

}
