package ru.tinkoff.edu.java.scrapper.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ChatRepository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private static final String FIND_CHAT_BY_ID = "SELECT * FROM chat WHERE id = ?";
    private static final String ADD_CHAT = "INSERT INTO chat VALUES (?)";
    private static final String DELETE_CHAT_BY_ID = "DELETE FROM chat where id = ?";
    private static final String FIND_ALL_CHATS = "SELECT * FROM chat";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Chat add(long chatId) {
        jdbcTemplate.update(ADD_CHAT, chatId);
        return findById(chatId);
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update(DELETE_CHAT_BY_ID, id) > 0;
    }

    @Override
    public Chat findById(long chatId) {
        return jdbcTemplate.queryForStream(FIND_CHAT_BY_ID, ps -> ps.setLong(1, chatId),
                BeanPropertyRowMapper.newInstance(Chat.class)).findFirst().orElse(null);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL_CHATS, new BeanPropertyRowMapper<>(Chat.class));
    }
}
