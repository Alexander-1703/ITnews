package ru.tinkoff.edu.java.scrapper.jooq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.environment.JooqIntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JooqChatRepositoryTest extends JooqIntegrationEnvironment {
    private static final long NOT_EXISTING_CHAT_ID = -1L;
    private static final long EXISTING_CHAT_ID = 0L;
    private static final int EXPECTED_FILLED_CHAT_SIZE = 3;
    private static final int EXPECTED_EMPTY_CHAT_SIZE = 0;

    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void addChat_notInDB_save() {
        assertNotNull(chatRepository.add(0L));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void addChat_existInDB_returnThisChat() {
        assertEquals(EXISTING_CHAT_ID, chatRepository.add(EXISTING_CHAT_ID).getId());
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    void removeChat_existsInDB_returnTrue() {
        assertTrue(chatRepository.remove(EXISTING_CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    void removeLink_notInDB_returnFalse() {
        assertFalse(chatRepository.remove(NOT_EXISTING_CHAT_ID));
    }

    @Sql(scripts = "/sql/chat/add_three_chats.sql")
    @Test
    @Transactional
    @Rollback
    void findAll_returnAllChats() {
        assertEquals(EXPECTED_FILLED_CHAT_SIZE, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void findAll_emptyChatTable_returnEmptyList() {
        assertEquals(EXPECTED_EMPTY_CHAT_SIZE, chatRepository.findAll().size());
    }
}
