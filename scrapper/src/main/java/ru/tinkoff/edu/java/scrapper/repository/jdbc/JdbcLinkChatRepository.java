package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkChatRepository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private static final String FIND_SUBSCRIPTION = "SELECT * FROM link_chat WHERE linkId = ? AND chatId = ?";
    private static final String ADD_LINK_TO_CHAT = "INSERT INTO link_chat VALUES (?, ?)";
    private static final String REMOVE_LINK_FROM_CHAT = "DELETE  FROM link_chat WHERE chatid = ? AND linkid = ?";
    private static final String FIND_CHATS_BY_LINK_ID =
            "SELECT chat.id FROM chat " +
                    "JOIN link_chat ON chat.id = link_chat.chatId " +
                    "WHERE link_chat.linkId = ?";
    private static final String FIND_LINKS_BY_CHAT_ID =
            "SELECT link.id, link.link, link.updatedAt FROM link " +
                    "JOIN link_chat ON link.id = link_chat.linkId " +
                    "WHERE link_chat.chatId = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLinkToChat(long linkId, long chatId) {
        if (!isSubscribed(linkId, chatId)) {
            return jdbcTemplate.update(ADD_LINK_TO_CHAT, linkId, chatId) > 0;
        }
        return false;
    }

    @Override
    public boolean removeLinkFromChat(long linkId, long chatId) {
        return jdbcTemplate.update(REMOVE_LINK_FROM_CHAT, chatId, linkId) > 0;
    }

    @Override
    public boolean isSubscribed(long linkId, long chatId) {
        return Boolean.TRUE.equals(jdbcTemplate.query(FIND_SUBSCRIPTION, ps -> {
            ps.setLong(1, linkId);
            ps.setLong(2, chatId);
        }, ResultSet::next));
    }

    @Override
    public List<Chat> findChatsByLinkId(long linkId) {
        return jdbcTemplate.query(FIND_CHATS_BY_LINK_ID, ps -> ps.setLong(1, linkId),
                BeanPropertyRowMapper.newInstance(Chat.class));
    }

    @Override
    public List<Link> findLinksByChatId(long chatId) {
        return jdbcTemplate.query(FIND_LINKS_BY_CHAT_ID, ps -> ps.setLong(1, chatId),
                BeanPropertyRowMapper.newInstance(Link.class));
    }
}
