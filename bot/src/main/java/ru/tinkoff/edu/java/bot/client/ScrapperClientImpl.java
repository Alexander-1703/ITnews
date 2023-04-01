package ru.tinkoff.edu.java.bot.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.request.LinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.dto.ApiErrorResponse;

@Slf4j
@RequiredArgsConstructor
public class ScrapperClientImpl implements ScrapperClient {
    private static final String CHAT_ID = "chat_id";
    private static final String TG_CHAT_URI = "/tg-chat/{id}";
    private static final String LINKS_URI = "/links";
    private final String url;
    private WebClient scrapperClient;

    @PostConstruct
    public void buildScrapperClient() {
        scrapperClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public Mono<Boolean> registerChat(long chatId) {
        return scrapperClient
                .post()
                .uri(TG_CHAT_URI, chatId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.error("Error" + apiErrorResponse.exceptionName() +
                                    "occurred: " + apiErrorResponse.exceptionMessage());
                            return Mono.error(new ResponseStatusException(
                                    response.statusCode(), apiErrorResponse.exceptionMessage()));
                        }))
                .bodyToMono(Void.class)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    @Override
    public Mono<Boolean> deleteChat(long chatId) {
        return scrapperClient
                .delete()
                .uri(TG_CHAT_URI, chatId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.error("Error" + apiErrorResponse.exceptionName() +
                                    "occurred: " + apiErrorResponse.exceptionMessage());
                            return Mono.error(new ResponseStatusException(
                                    response.statusCode(), apiErrorResponse.exceptionMessage()));
                        }))
                .bodyToMono(Void.class)
                .thenReturn(true)
                .onErrorReturn(false);
    }

    @Override
    public Mono<ListLinkResponse> getLinks(long chatId) {
        return scrapperClient
                .get()
                .uri(LINKS_URI)
                .header(CHAT_ID, String.valueOf(chatId))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.error("Error" + apiErrorResponse.exceptionName() +
                                    "occurred: " + apiErrorResponse.exceptionMessage());
                            return Mono.empty();
                        }))
                .bodyToMono(ListLinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> addLink(long chatId, String url) {
        return scrapperClient
                .post()
                .uri(LINKS_URI)
                .header(CHAT_ID, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new LinkRequest(url)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.error("Error" + apiErrorResponse.exceptionName() +
                                    "occurred: " + apiErrorResponse.exceptionMessage());
                            return Mono.empty();
                        }))
                .bodyToMono(LinkResponse.class);
    }

    @Override
    public Mono<LinkResponse> removeLink(long chatId, String url) {
        return scrapperClient
                .method(HttpMethod.DELETE)
                .uri(LINKS_URI)
                .header(CHAT_ID, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new LinkRequest(url)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.error("Error" + apiErrorResponse.exceptionName() +
                                    "occurred: " + apiErrorResponse.exceptionMessage());
                            return Mono.empty();
                        }))
                .bodyToMono(LinkResponse.class);
    }
}
