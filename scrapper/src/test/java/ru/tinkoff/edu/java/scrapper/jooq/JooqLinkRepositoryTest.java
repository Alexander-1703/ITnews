package ru.tinkoff.edu.java.scrapper.jooq;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.JooqIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JooqLinkRepositoryTest extends JooqIntegrationEnvironment {
    private static final long NOT_EXISTING_LINK_ID = -1L;
    private static final int EXPECTED_FILLED_LINK_SIZE = 3;
    private static final int EXPECTED_EMPTY_LINK_SIZE = 0;
    private static final String TEST_LINK = "http://test-link.ru";

    @Autowired
    private JooqLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void addLink_notInDB_save() {
        Link link = new Link();
        link.setLink(TEST_LINK);
        assertNotNull(linkRepository.save(link));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    void addLink_existInDB_returnThisLink() {
        Link link = new Link();
        link.setLink(TEST_LINK);
        assertEquals(TEST_LINK, linkRepository.save(link).getLink());
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    void removeLink_existsInDB_returnTrue() {
        List<Link> links = linkRepository.findAll();
        assertTrue(linkRepository.remove(links.get(0).getId()));
    }

    @Test
    @Transactional
    @Rollback
    void removeLink_notInDB_returnFalse() {
        assertFalse(linkRepository.remove(NOT_EXISTING_LINK_ID));
    }

    @Sql(scripts = "/sql/link/add_three_links.sql")
    @Test
    @Transactional
    @Rollback
    void findAll_returnAllLinks() {
        assertEquals(EXPECTED_FILLED_LINK_SIZE, linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll_emptyLinkTable_returnEmptyList() {
        assertEquals(EXPECTED_EMPTY_LINK_SIZE, linkRepository.findAll().size());
    }
}
