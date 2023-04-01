package ru.tinkoff.edu.java.bot.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinkResponse;

public interface ScrapperClient {
    boolean registerChat(long chatId);

    boolean deleteChat(long chatId);

    ListLinkResponse getLinks(long chatId);

    LinkResponse addLink(long chatId, String url);

    LinkResponse removeLink(long chatId, String url);
}
