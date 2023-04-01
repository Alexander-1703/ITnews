package ru.tinkoff.edu.java.bot.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinkResponse;

public interface ScrapperClient {
    Mono<Boolean> registerChat(long chatId);

    Mono<Boolean> deleteChat(long chatId);

    Mono<ListLinkResponse> getLinks(long chatId);

    Mono<LinkResponse> addLink(long chatId, String url);

    Mono<LinkResponse> removeLink(long chatId, String url);
}
