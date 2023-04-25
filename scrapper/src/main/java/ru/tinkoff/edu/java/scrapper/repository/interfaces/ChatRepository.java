package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Chat;

public interface ChatRepository {
    Chat add(long chatId);

    boolean remove(long id);

    Chat findById(long chatId);

    List<Chat> findAll();
}
