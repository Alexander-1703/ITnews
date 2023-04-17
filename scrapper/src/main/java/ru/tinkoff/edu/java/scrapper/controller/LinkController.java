package ru.tinkoff.edu.java.scrapper.controller;

import java.net.URI;
import java.util.List;
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
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.dto.response.request.LinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkRepository;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {
    private static final String CHAT_ID = "chat_id";

    private final LinkRepository linkRepository;
    private final LinkChatRepository subscription;

    @GetMapping()
    public ListLinkResponse getLinks(@RequestHeader(CHAT_ID) @PositiveOrZero Long id) {
        List<LinkResponse> response = subscription.findLinksByChatId(id).stream()
                .map(link -> new LinkResponse(link.getId(), URI.create(link.getLink())))
                .toList();
        return new ListLinkResponse(response, response.size());
    }

    @PostMapping()
    public LinkResponse addLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest addRequest) {
        Link link = new Link();
        link.setLink(addRequest.link());
        link = linkRepository.save(link);
        subscription.addLinkToChat(link.getId(), id);
        return new LinkResponse(link.getId(), URI.create(link.getLink()));
    }

    @DeleteMapping()
    public LinkResponse removeLink(@RequestHeader(CHAT_ID) @PositiveOrZero Long id, @RequestBody LinkRequest removeRequest) {
        Link link = linkRepository.findByLink(removeRequest.link());
        if (link != null) {
            linkRepository.remove(id);
            return new LinkResponse(link.getId(), URI.create(link.getLink()));
        }
        throw new NoSuchElementException("No element with such id");
    }

}
