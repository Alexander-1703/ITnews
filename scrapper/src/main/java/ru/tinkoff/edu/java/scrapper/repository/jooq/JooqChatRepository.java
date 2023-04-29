package ru.tinkoff.edu.java.scrapper.repository.jooq;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext context;

    @Override
    @Transactional
    public Chat add(long chatId) {
        return context.insertInto(CHAT)
                .set(CHAT.ID, chatId)
                .onConflict(CHAT.ID)
                .doUpdate()
                .set(CHAT.ID, CHAT.ID)
                .returning(CHAT.fields())
                .fetchOneInto(Chat.class);
    }

    @Override
    @Transactional
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
