package ru.tinkoff.edu.java.scrapper.controller;

import java.util.NoSuchElementException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping("/tg-chat")
public class TgChatController {

    @PostMapping("/{id}")
    public void registerChat(@PathVariable @PositiveOrZero Long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id can`t be negative");
        }
        //registration
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable @PositiveOrZero Long id) {
        if (id < 0) {
            throw new NoSuchElementException("No such element id");
        }
        //removal
    }

}
