package ru.tinkoff.edu.java.scrapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.JdbcIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback
public class JdbcChatRepositoryTest extends JdbcIntegrationEnvironment {
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    void addChat_notInDB_save() {
        Chat chat = new Chat();
        chat.setId(0L);
        int expectedUpdatedRows = 1;
        assertEquals(expectedUpdatedRows, chatRepository.add(chat));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    void addChat_existInDB_throwException() {
        Chat chat = new Chat();
        Long existingId = 0L;
        chat.setId(existingId);
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.add(chat));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    void removeChat_existsInDB_success() {
        long existingId = 0L;
        int expectedDeletedRows = 1;
        assertEquals(expectedDeletedRows, chatRepository.remove(existingId));
    }

    @Test
    void removeLink_notInDB_changed0() {
        int notExistingChatId = 0;
        int expectedDeletedRows = 0;
        assertEquals(expectedDeletedRows, chatRepository.remove(notExistingChatId));
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
