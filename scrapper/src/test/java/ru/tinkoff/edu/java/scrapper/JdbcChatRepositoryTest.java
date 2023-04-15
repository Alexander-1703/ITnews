package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.tinkoff.edu.java.scrapper.environment.JdbcIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;

public class JdbcChatRepositoryTest extends JdbcIntegrationEnvironment {
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    void test() {
        chatRepository.findAll();
    }
}
