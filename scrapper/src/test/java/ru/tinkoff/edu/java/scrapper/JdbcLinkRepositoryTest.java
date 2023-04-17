package ru.tinkoff.edu.java.scrapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.JdbcIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JdbcLinkRepositoryTest extends JdbcIntegrationEnvironment {
    private static final long NOT_EXISTING_LINK_ID = -1L;
    private static final int EXPECTED_FILLED_LINK_SIZE = 3;
    private static final int EXPECTED_EMPTY_LINK_SIZE = 0;

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    void addLink_notInDB_save() {
        Link link = new Link();
        link.setLink("http://test-link.ru");
        assertNotNull(linkRepository.save(link));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void addLink_existInDB_nothingAdded() {
        int tableSizeBeforeAdd = linkRepository.findAll().size();
        Link link = new Link();
        link.setLink("http://test-link.ru");
        linkRepository.save(link);
        assertEquals(tableSizeBeforeAdd, linkRepository.findAll().size());
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void removeLink_existsInDB_returnTrue() {
        List<Link> links = linkRepository.findAll();
        assertTrue(linkRepository.remove(links.get(0).getId()));
    }

    @Test
    void removeLink_notInDB_returnFalse() {
        assertFalse(linkRepository.remove(NOT_EXISTING_LINK_ID));
    }

    @Sql(scripts = "/sql/link/add_three_links.sql")
    @Test
    void findAll_returnAllLinks() {
        assertEquals(EXPECTED_FILLED_LINK_SIZE, linkRepository.findAll().size());
    }

    @Test
    void findAll_emptyLinkTable_returnEmptyList() {
        assertEquals(EXPECTED_EMPTY_LINK_SIZE, linkRepository.findAll().size());
    }
}
