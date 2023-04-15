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
    public int add(Chat chat) {
        if (chat.getId() == null) {
            return jdbcTemplate.update("INSERT INTO chat DEFAULT VALUES");
        }
        return jdbcTemplate.update("INSERT INTO chat VALUES (?)", chat.getId());
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update("DELETE FROM chat where id = ?", id);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }
}
