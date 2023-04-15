package ru.tinkoff.edu.java.scrapper.repository;

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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addLinkToChat(long linkId, long chatId) {
        return jdbcTemplate.update("INSERT INTO link_chat VALUES (?, ?)", linkId, chatId);
    }

    @Override
    public int removeLinkFromChat(long linkId, long chatId) {
        return jdbcTemplate.update("DELETE  FROM link_chat WHERE chatid = ? AND linkid = ?", chatId, linkId);
    }

    @Override
    public List<Chat> findChatsByLinkId(long linkId) {
        String sql = "SELECT chat.id FROM chat " +
                "JOIN link_chat ON chat.id = link_chat.chatId " +
                "WHERE link_chat.linkId = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, linkId),
                BeanPropertyRowMapper.newInstance(Chat.class));
    }

    @Override
    public List<Link> findLinksByChatId(long chatId) {
        String sql = "SELECT link.id, link.link, link.updatedAt FROM link " +
                "JOIN link_chat ON link.id = link_chat.linkId " +
                "WHERE link_chat.chatId = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, chatId),
                BeanPropertyRowMapper.newInstance(Link.class));
    }
}
