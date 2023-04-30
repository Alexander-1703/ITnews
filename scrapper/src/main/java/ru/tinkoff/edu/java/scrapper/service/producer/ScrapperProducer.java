package ru.tinkoff.edu.java.scrapper.service.producer;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public interface ScrapperProducer {
    boolean postUpdate(LinkUpdateRequest request);
}
