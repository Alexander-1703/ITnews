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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
public class JdbcLinkRepositoryTest extends JdbcIntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    void addLink_notInDB_save() {
        Link link = new Link();
        link.setLink("http://test-link.ru");
        int expectedUpdatedRows = 1;
        assertEquals(expectedUpdatedRows, linkRepository.add(link));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void addLink_existInDB_throwException() {
        Link link = new Link();
        link.setLink("http://test-link.ru");
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(link));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    void removeLink_existsInDB_success() {
        List<Link> links = linkRepository.findAll();
        int expectedDeletedRows = 1;
        assertEquals(expectedDeletedRows, linkRepository.remove(links.get(0).getId()));
    }

    @Test
    void removeLink_notInDB_changed0() {
        long notExistingLinkId = 0;
        int expectedDeletedRows = 0;
        assertEquals(expectedDeletedRows, linkRepository.remove(notExistingLinkId));
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
