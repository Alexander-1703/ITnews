package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.mapper.ChatRowMapper;
import ru.tinkoff.edu.java.scrapper.repository.mapper.LinkRowMapper;

@RequiredArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private static final String ADD_LINK_TO_CHAT = "INSERT INTO link_chat VALUES (?, ?)";
    private static final String REMOVE_LINK_FROM_CHAT = "DELETE  FROM link_chat WHERE chatid = ? AND linkid = ?";
    private static final String FIND_SUBSCRIPTION =
            "SELECT link_chat.linkid, link_chat.chatid" +
                    " FROM link_chat" +
                    " WHERE linkId = ? AND chatId = ?";
    private static final String FIND_CHATS_BY_LINK_ID =
            "SELECT * FROM chat " +
                    "JOIN link_chat ON chat.id = link_chat.chatId " +
                    "WHERE link_chat.linkId = ?";
    private static final String FIND_LINKS_BY_CHAT_ID =
            "SELECT * FROM link " +
                    "JOIN link_chat ON link.id = link_chat.linkId " +
                    "WHERE link_chat.chatId = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkRowMapper = new LinkRowMapper();
    private final RowMapper<Chat> chatRowMapper = new ChatRowMapper();

    @Override
    @Transactional
    public boolean addLinkToChat(long linkId, long chatId) {
        if (!isSubscribed(linkId, chatId)) {
            return jdbcTemplate.update(ADD_LINK_TO_CHAT, linkId, chatId) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean removeLinkFromChat(long linkId, long chatId) {
        return jdbcTemplate.update(REMOVE_LINK_FROM_CHAT, chatId, linkId) > 0;
    }

    @Override
    public boolean isSubscribed(long linkId, long chatId) {
        Boolean isSubscribed = jdbcTemplate.query(FIND_SUBSCRIPTION, ps -> {
            ps.setLong(1, linkId);
            ps.setLong(2, chatId);
        }, ResultSet::next);
        return isSubscribed != null && isSubscribed;
    }

    @Override
    public List<Chat> findChatsByLinkId(long linkId) {
        return jdbcTemplate.query(FIND_CHATS_BY_LINK_ID, ps -> ps.setLong(1, linkId),
                chatRowMapper);
    }

    @Override
    public List<Link> findLinksByChatId(long chatId) {
        return jdbcTemplate.query(FIND_LINKS_BY_CHAT_ID, ps -> ps.setLong(1, chatId),
                linkRowMapper);
    }
}
