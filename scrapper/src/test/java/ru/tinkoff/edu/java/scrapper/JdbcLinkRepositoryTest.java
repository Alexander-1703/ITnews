package ru.tinkoff.edu.java.scrapper;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

@SpringBootTest(classes = IntegrationEnvironment.TestDataSourceConfiguration.class)
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Transactional
    @Rollback
    @Test
    void addLink_notInDB_save() {
        Link link = new Link();
        link.setLink("http://test-link.ru");
        linkRepository.add(link);
        List<Link> links = linkRepository.findAll();
        Assertions.assertEquals(1, links.size());
    }

    @Transactional
    @Rollback
    @Sql("INSERT INTO link(link) VALUES('http://test-link.ru')")
    @Test
    void addLink_existInDB_throwException() {
        Link link = new Link();
        link.setLink("http://test-link.ru");
        linkRepository.add(link);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> linkRepository.add(link));
    }

    @Transactional
    @Rollback
    @Sql("INSERT INTO link(link) VALUES('http://test-link.ru')")
    @Test
    void removeLink_existsInDB_success() {
        List<Link> links = linkRepository.findAll();
        linkRepository.remove(links.get(0).getId());
        Assertions.assertEquals(0, linkRepository.findAll().size());
    }
}
