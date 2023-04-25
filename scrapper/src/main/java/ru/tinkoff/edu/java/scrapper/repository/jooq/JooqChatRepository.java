package ru.tinkoff.edu.java.scrapper.repository.jooq;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.jooq.DSLContext;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ChatRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext context;

    @Override
    public Chat add(long chatId) {
        return context.insertInto(CHAT)
                .set(CHAT.ID, chatId)
                .returning(CHAT.fields())
                .fetchOneInto(Chat.class);
    }

    @Override
    public boolean remove(long id) {
        return context.deleteFrom(CHAT)
                .where(CHAT.ID.eq(id))
                .execute() > 0;
    }

    @Override
    public Chat findById(long chatId) {
        return context.select(CHAT.fields())
                .from(CHAT)
                .where(CHAT.ID.eq(chatId))
                .fetchOneInto(Chat.class);
    }

    @Override
    public List<Chat> findAll() {
        return context.select(CHAT.fields())
                .from(CHAT)
                .fetchInto(Chat.class);
    }
}
