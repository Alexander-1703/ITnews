package ru.tinkoff.edu.java.bot.service;

import java.util.List;

import ru.tinkoff.edu.java.dto.response.LinkResponse;

public interface LinkService {

    List<LinkResponse> getLinks(long chatId);

    LinkResponse trackLink(long chatId, String link);

    LinkResponse untrackLink(long chatId, String link);
}
