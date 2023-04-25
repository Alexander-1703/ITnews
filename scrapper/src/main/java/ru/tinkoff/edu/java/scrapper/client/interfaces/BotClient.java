package ru.tinkoff.edu.java.scrapper.client.interfaces;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface BotClient {
    boolean postUpdate(LinkUpdateRequest request);
}
