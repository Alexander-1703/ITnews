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
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Chat add(long chatId) {
        jdbcTemplate.update("INSERT INTO chat VALUES (?)", chatId);
        return findById(chatId);
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update("DELETE FROM chat where id = ?", id) > 0;
    }

    @Override
    public Chat findById(long chatId) {
        String sql = "SELECT * FROM chat WHERE id = ?";
        return jdbcTemplate.queryForStream(sql, ps -> ps.setLong(1, chatId),
                BeanPropertyRowMapper.newInstance(Chat.class)).findFirst().orElse(null);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }
}
