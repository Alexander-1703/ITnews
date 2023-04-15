package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.JdbcIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@Rollback
public class JdbcChatRepositoryTest extends JdbcIntegrationEnvironment {
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    void addChat_notInDB_save() {
        assertNotNull(chatRepository.add(0L));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    void addChat_existInDB_throwException() {
        long existingId = 0L;
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.add(existingId));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    void removeChat_existsInDB_returnTrue() {
        long existingId = 0L;
        assertTrue(chatRepository.remove(existingId));
    }

    @Test
    void removeLink_notInDB_returnFalse() {
        int notExistingChatId = 0;
        assertFalse(chatRepository.remove(notExistingChatId));
    }

    @Sql(scripts = "/sql/chat/add_three_chats.sql")
    @Test
    void findAll_returnAllChats() {
        int expectedSize = 3;
        assertEquals(expectedSize, chatRepository.findAll().size());
    }

    @Test
    void findAll_emptyChatTable_returnEmptyList() {
        int expectedSize = 0;
        assertEquals(expectedSize, chatRepository.findAll().size());
    }
}
