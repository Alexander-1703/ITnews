package ru.tinkoff.edu.java.scrapper.repository.jooq;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkChatRepository;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Link.LINK;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.LinkChat.LINK_CHAT;

@RequiredArgsConstructor
public class JooqLinkChatRepository implements LinkChatRepository {
    private final DSLContext context;

    @Override
    @Transactional
    public boolean addLinkToChat(long linkId, long chatId) {
        if (!isSubscribed(linkId, chatId)) {
            return context.insertInto(LINK_CHAT)
                    .set(LINK_CHAT.LINKID, linkId)
                    .set(LINK_CHAT.CHATID, chatId)
                    .execute() > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean removeLinkFromChat(long linkId, long chatId) {
        return context.deleteFrom(LINK_CHAT)
                .where(LINK_CHAT.CHATID.eq(chatId), LINK_CHAT.LINKID.eq(linkId))
                .execute() > 0;
    }

    @Override
    public boolean isSubscribed(long linkId, long chatId) {
        return context.select(LINK_CHAT.fields())
                .from(LINK_CHAT)
                .where(LINK_CHAT.LINKID.eq(linkId), LINK_CHAT.CHATID.eq(chatId))
                .fetchOne() != null;
    }

    @Override
    public List<Chat> findChatsByLinkId(long linkId) {
        return context.select(CHAT.fields())
                .from(CHAT)
                .join(LINK_CHAT)
                .on(CHAT.ID.eq(LINK_CHAT.CHATID))
                .where(LINK_CHAT.LINKID.eq(linkId))
                .fetchInto(Chat.class);
    }

    @Override
    public List<Link> findLinksByChatId(long chatId) {
        return context.select(LINK.fields())
                .from(LINK)
                .join(LINK_CHAT)
                .on(LINK.ID.eq(LINK_CHAT.LINKID))
                .where(LINK_CHAT.CHATID.eq(chatId))
                .fetchInto(Link.class);
    }
}
