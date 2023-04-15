package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Chat;

public interface ChatRepository {
    int add(Chat chat);

    int remove(long id);

    List<Chat> findAll();
}
