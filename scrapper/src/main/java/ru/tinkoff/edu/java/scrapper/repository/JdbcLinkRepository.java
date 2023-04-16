package ru.tinkoff.edu.java.scrapper.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    public Link add(String link) {
        jdbcTemplate.update("INSERT INTO link(link) VALUES (?)", link);
        return findByLink(link);
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update("DELETE FROM link WHERE id = ?", id) > 0;
    }

    @Override
    public Link findById(long linkId) {
        String sql = "SELECT * FROM link WHERE id = ?";
        return jdbcTemplate.queryForStream(sql, ps -> ps.setLong(1, linkId),
                BeanPropertyRowMapper.newInstance(Link.class)).findFirst().orElse(null);
    }

    @Override
    public Link findByLink(String link) {
        String sql = "SELECT * FROM link WHERE link = ?";
        return jdbcTemplate.queryForStream(sql, ps -> ps.setString(1, link),
                BeanPropertyRowMapper.newInstance(Link.class)).findFirst().orElse(null);
    }

    @Override
    public List<Link> findNotUpdated(Duration interval) {
        List<Link> linkList = findAll();
        linkList.sort(Comparator.comparing(
                link -> Duration.between(LocalDateTime.now(), link.getUpdatedAt()).compareTo(interval) > 0));
        return linkList;
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", new BeanPropertyRowMapper<>(Link.class));
    }
}
