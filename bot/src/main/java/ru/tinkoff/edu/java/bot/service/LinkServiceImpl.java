package ru.tinkoff.edu.java.bot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.interfaces.LinkService;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {
    private final ScrapperClient scrapperClient;

    public List<LinkResponse> getLinks(long chatId) {
        return scrapperClient.getLinks(chatId).linkList();
    }

    public LinkResponse trackLink(long chatId, String link) {
        return scrapperClient.addLink(chatId, link);
    }

    public LinkResponse untrackLink(long chatId, String link) {
        return scrapperClient.removeLink(chatId, link);
    }
}
