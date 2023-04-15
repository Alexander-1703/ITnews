package ru.tinkoff.edu.java.scrapper.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.LinkRepository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int add(Link link) {
        if (link.getId() == null) {
            return jdbcTemplate.update("INSERT INTO link(link) VALUES (?)", link.getLink());
        }
        return jdbcTemplate.update("INSERT INTO link VALUES (?,?,?)", link.getId(), link.getLink(), link.getUpdatedAt());
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update("DELETE FROM link WHERE id = ?", id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", new BeanPropertyRowMapper<>(Link.class));
    }
}
