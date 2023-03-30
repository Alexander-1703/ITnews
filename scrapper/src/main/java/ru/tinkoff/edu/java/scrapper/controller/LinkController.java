package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkRequest;

@RestController
@RequestMapping("/links")
public class LinkController {

    @GetMapping()
    public String getLinks(@RequestBody LinkRequest getRequest) {
        return "Ссылки успешно получены";
    }

    @PostMapping()
    public String addLink(@RequestBody LinkRequest addRequest) {
        return "Ссылка успешно добавлена";
    }

    @DeleteMapping()
    public String removeLink(@RequestBody LinkRequest removeRequest) {
        return "Ссылка успешно убрана";
    }
}
