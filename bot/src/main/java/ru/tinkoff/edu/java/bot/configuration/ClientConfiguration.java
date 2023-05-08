package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.client.ScrapperClientImpl;

@Configuration
public class ClientConfiguration {
    private final String scrapperUrl;

    public ClientConfiguration(@Value("${client.scrapper.url:http://localhost:8080}") String scrapperUrl) {
        this.scrapperUrl = scrapperUrl;
    }

    @Bean
    public ScrapperClient scrapperWebClient() {
        return new ScrapperClientImpl(scrapperUrl);
    }

}
