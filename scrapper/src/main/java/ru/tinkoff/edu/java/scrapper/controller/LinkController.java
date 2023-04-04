package ru.tinkoff.edu.java.scrapper.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.PositiveOrZero;
import ru.tinkoff.edu.java.scrapper.dto.response.request.LinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.response.ListLinkResponse;

@RestController
@Validated
@RequestMapping("/links")
public class LinkController {
    private static final String CHAT_ID = "chat_id";

    @GetMapping()
    public ListLinkResponse getLinks(@RequestHeader(CHAT_ID) @PositiveOrZero Long id) {
        return new ListLinkResponse(new ArrayList<>(), 0);
    }

    @PostMapping()
    public LinkResponse addLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest addRequest) {
        return new LinkResponse(0L, URI.create(addRequest.link()));
    }

    @DeleteMapping()
    public LinkResponse removeLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest removeRequest) {
        throw new NoSuchElementException("No element with such id");
    }
}
