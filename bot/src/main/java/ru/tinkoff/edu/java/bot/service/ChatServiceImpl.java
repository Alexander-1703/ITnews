package ru.tinkoff.edu.java.bot.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.service.interfaces.ChatService;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ScrapperClient scrapperClient;

    @Override
    public boolean registerChat(long chatId) {
        return scrapperClient.registerChat(chatId);
    }

    @Override
    public boolean deleteChat(long chatId) {
        return scrapperClient.deleteChat(chatId);
    }
}
