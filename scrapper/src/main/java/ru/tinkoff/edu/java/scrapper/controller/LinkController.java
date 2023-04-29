package ru.tinkoff.edu.java.scrapper.controller;

import java.net.URI;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {
    private static final String CHAT_ID = "chat_id";

    private final LinkService linkService;

    @GetMapping()
    public ListLinkResponse getLinks(@RequestHeader(CHAT_ID) @PositiveOrZero Long id) {
        List<LinkResponse> response = linkService.listAll(id).stream()
                .map(link -> new LinkResponse(link.getId(), URI.create(link.getLink())))
                .toList();
        return new ListLinkResponse(response, response.size());
    }

    @PostMapping()
    public LinkResponse addLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest addRequest) {
        URI uri = URI.create(addRequest.link());
        Link link = linkService.add(id, uri);
        return new LinkResponse(link.getId(), uri);
    }

    @DeleteMapping()
    public LinkResponse removeLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest removeRequest) {
        URI uri = URI.create(removeRequest.link());
        Link link = linkService.remove(id, uri);
        return new LinkResponse(link.getId(), uri);
    }
}
