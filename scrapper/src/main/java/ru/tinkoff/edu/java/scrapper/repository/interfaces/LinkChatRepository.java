package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import java.util.List;

import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;

public interface LinkChatRepository {
    boolean addLinkToChat(long linkId, long chatId);

    boolean removeLinkFromChat(long linkId, long chatId);

    List<Chat> findChatsByLinkId(long linkId);

    List<Link> findLinksByChatId(long chatId);
}
