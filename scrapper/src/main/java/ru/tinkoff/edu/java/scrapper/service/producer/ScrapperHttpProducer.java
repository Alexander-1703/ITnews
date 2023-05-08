package ru.tinkoff.edu.java.scrapper.service.producer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.client.interfaces.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "scrapper", name = "use-queue", havingValue = "false", matchIfMissing = true)
public class ScrapperHttpProducer implements ScrapperProducer {
    private final BotClient botClient;

    @Override
    public boolean postUpdate(LinkUpdateRequest request) {
        return botClient.postUpdate(request);
    }
}
