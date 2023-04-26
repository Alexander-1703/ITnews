package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.mapper.LinkRowMapper;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private static final String ADD_LINK = "INSERT INTO link(link) VALUES (?)";
    private static final String DELETE_LINK_BY_ID = "DELETE FROM link WHERE id = ?";
    private static final String FIND_LINK_BY_ID = "SELECT * FROM link WHERE id = ?";
    private static final String FIND_LINK_BY_LINK = "SELECT * FROM link WHERE link = ?";
    private static final String FIND_ALL_LINKS = "SELECT * FROM link";
    private static final String UPDATE_LINK =
            "UPDATE link SET link = ?, updatedat = ?, ghforks = ?, ghbranches = ?, soanswers = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkRowMapper = new LinkRowMapper();

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            Link alreadySaved = findByLink(link.getLink());
            if (alreadySaved != null) {
                return alreadySaved;
            }
            jdbcTemplate.update(ADD_LINK, link.getLink());
            return findByLink(link.getLink());
        }

        jdbcTemplate.update(UPDATE_LINK, link.getLink(), link.getUpdatedAt(), link.getGhForksCount(),
                link.getGhBranchesCount(), link.getSoAnswersCount(), link.getId());
        return link;
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update(DELETE_LINK_BY_ID, id) > 0;
    }

    @Override
    public Link findById(long linkId) {
        return jdbcTemplate.queryForStream(FIND_LINK_BY_ID, ps -> ps.setLong(1, linkId),
                linkRowMapper).findFirst().orElse(null);
    }

    @Override
    public Link findByLink(String link) {
        return jdbcTemplate.queryForStream(FIND_LINK_BY_LINK, ps -> ps.setString(1, link),
                linkRowMapper).findFirst().orElse(null);
    }

    @Override
    public List<Link> findNotUpdated(Duration interval) {
        return findAll().stream()
                .filter(link -> Duration.between(link.getUpdatedAt(), OffsetDateTime.now()).compareTo(interval) > 0)
                .toList();
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_LINKS, linkRowMapper);
    }
}
