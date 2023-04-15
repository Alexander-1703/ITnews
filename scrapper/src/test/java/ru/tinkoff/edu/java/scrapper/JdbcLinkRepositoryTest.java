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
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    void addLink_notInDB_save() {
        assertNotNull(linkRepository.add("http://test-link.ru"));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void addLink_existInDB_throwException() {
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add("http://test-link.ru"));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void removeLink_existsInDB_returnTrue() {
        List<Link> links = linkRepository.findAll();
        assertTrue(linkRepository.remove(links.get(0).getId()));
    }

    @Test
    void removeLink_notInDB_returnFalse() {
        long notExistingLinkId = 0;
        assertFalse(linkRepository.remove(notExistingLinkId));
    }

    @Sql(scripts = "/sql/link/add_three_links.sql")
    @Test
    void findAll_returnAllLinks() {
        int expectedSize = 3;
        assertEquals(expectedSize, linkRepository.findAll().size());
    }

    @Test
    void findAll_emptyLinkTable_returnEmptyList() {
        int expectedSize = 0;
        assertEquals(expectedSize, linkRepository.findAll().size());
    }
}
