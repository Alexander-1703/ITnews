package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getLinks(@RequestBody LinkRequest getRequest) {
        return ResponseEntity.ok("Ссылки успешно получены");
    }

    @PostMapping()
    public ResponseEntity<String> addLink(@RequestBody LinkRequest addRequest) {
        return ResponseEntity.ok("Ссылка успешно добавлена");
    }

    @DeleteMapping()
    public ResponseEntity<String> removeLink(@RequestBody LinkRequest removeRequest) {
        return ResponseEntity.ok("Ссылка успешно убрана");
    }
}
