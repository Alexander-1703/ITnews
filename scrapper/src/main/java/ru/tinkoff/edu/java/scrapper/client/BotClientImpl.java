package ru.tinkoff.edu.java.scrapper.client;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import ru.tinkoff.edu.java.scrapper.client.interfaces.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

public class BotClientImpl implements BotClient {
    private static final String BOT_URI = "/updates";

    private final String url;
    private WebClient botWebClient;

    public BotClientImpl(String url) {
        this.url = url;
    }

    @PostConstruct
    public void buildBotWebClient() {
        botWebClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }


    @Override
    public boolean postUpdate(LinkUpdateRequest request) {
        HttpStatusCode httpStatusCode = Objects.requireNonNull(botWebClient.post()
                        .uri(BOT_URI)
                        .bodyValue(request)
                        .retrieve()
                        .toBodilessEntity()
                        .block())
                .getStatusCode();
        return httpStatusCode == HttpStatus.OK;
    }
}
