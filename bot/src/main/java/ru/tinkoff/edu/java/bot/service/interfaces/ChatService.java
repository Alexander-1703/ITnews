package ru.tinkoff.edu.java.bot.service.interfaces;

public interface ChatService {
    boolean registerChat(long chatId);

    boolean deleteChat(long chatId);
}
