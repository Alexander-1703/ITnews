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
    private static final String ADD_LINK = "INSERT INTO link(link) VALUES (?)";
    private static final String DELETE_LINK_BY_ID = "DELETE FROM link WHERE id = ?";
    private static final String FIND_LINK_BY_ID = "SELECT * FROM link WHERE id = ?";
    private static final String FIND_LINK_BY_LINK = "SELECT * FROM link WHERE link = ?";
    private static final String FIND_ALL_LINKS = "SELECT * FROM link";
    private static final String UPDATE_LINK = "UPDATE link SET link = ?, updatedAt = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            jdbcTemplate.update(ADD_LINK, link.getLink());
            return findByLink(link.getLink());
        }
        jdbcTemplate.update(UPDATE_LINK, link.getLink(), link.getUpdatedAt(), link.getId());
        return link;
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update(DELETE_LINK_BY_ID, id) > 0;
    }

    @Override
    public Link findById(long linkId) {
        return jdbcTemplate.queryForStream(FIND_LINK_BY_ID, ps -> ps.setLong(1, linkId),
                BeanPropertyRowMapper.newInstance(Link.class)).findFirst().orElse(null);
    }

    @Override
    public Link findByLink(String link) {
        return jdbcTemplate.queryForStream(FIND_LINK_BY_LINK, ps -> ps.setString(1, link),
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
        return jdbcTemplate.query(FIND_ALL_LINKS, new BeanPropertyRowMapper<>(Link.class));
    }
}
