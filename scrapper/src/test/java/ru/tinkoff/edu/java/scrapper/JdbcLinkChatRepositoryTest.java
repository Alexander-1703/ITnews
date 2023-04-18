package ru.tinkoff.edu.java.scrapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcLinkChatRepositoryTest extends JdbcChatRepositoryTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final int EXPECTED_FILLED_TABLE_SIZE = 3;
    private static final int EXPECTED_EMPTY_TABLE_SIZE = 0;

    @Autowired
    private JdbcLinkChatRepository linkChatRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_chatExist_returnTrue() {
        long chatId = chatRepository.findAll().get(0).getId();
        long linkId = linkRepository.findAll().get(0).getId();
        assertTrue(linkChatRepository.addLinkToChat(linkId, chatId));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_chatNotExist_throwsException() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertThrows(DataIntegrityViolationException.class,
                () -> linkChatRepository.addLinkToChat(linkId, NOT_EXISTING_ID));
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_chatExist_returnTrue() {
        long chatId = chatRepository.findAll().get(0).getId();
        long linkId = linkRepository.findAll().get(0).getId();
        linkChatRepository.addLinkToChat(linkId, chatId);
        assertTrue(linkChatRepository.removeLinkFromChat(linkId, chatId));
    }

    @Sql(scripts = "/sql/link/add_test_link.sql")
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_chatNotExist_returnFalse() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertFalse(linkChatRepository.removeLinkFromChat(linkId, NOT_EXISTING_ID));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    public void addLinkToChat_linkNotExist_throwsException() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertThrows(DataIntegrityViolationException.class,
                () -> linkChatRepository.addLinkToChat(NOT_EXISTING_ID, chatId));
    }

    @Sql(scripts = "/sql/chat/add_chat_with_id_0.sql")
    @Test
    @Transactional
    @Rollback
    public void removeLinkFromChat_linkNotExist_returnFalse() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertFalse(linkChatRepository.removeLinkFromChat(NOT_EXISTING_ID, chatId));
    }


    @Sql(scripts = {"/sql/chat/add_three_chats.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void linkId_findChatsByLinkId_listOfChats() {
        long linkId = linkRepository.findAll().get(0).getId();
        List<Long> listChatId = chatRepository.findAll().stream().map(Chat::getId).toList();
        for (var chatId : listChatId) {
            linkChatRepository.addLinkToChat(linkId, chatId);
        }
        assertEquals(EXPECTED_FILLED_TABLE_SIZE, linkChatRepository.findChatsByLinkId(linkId).size());
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_three_links.sql"})
    @Test
    @Transactional
    @Rollback
    public void chatId_findLinksByChatId_listOfLinks() {
        long chatId = chatRepository.findAll().get(0).getId();
        List<Long> listLinkId = linkRepository.findAll().stream().map(Link::getId).toList();
        for (var linkId : listLinkId) {
            linkChatRepository.addLinkToChat(linkId, chatId);
        }
        assertEquals(EXPECTED_FILLED_TABLE_SIZE, linkChatRepository.findLinksByChatId(chatId).size());
    }

    @Sql(scripts = {"/sql/chat/add_three_chats.sql", "/sql/link/add_test_link.sql"})
    @Test
    @Transactional
    @Rollback
    public void linkId_findChatsByLinkId_emptyListOfChats() {
        long linkId = linkRepository.findAll().get(0).getId();
        assertEquals(EXPECTED_EMPTY_TABLE_SIZE, linkChatRepository.findChatsByLinkId(linkId).size());
    }

    @Sql(scripts = {"/sql/chat/add_chat_with_id_0.sql", "/sql/link/add_three_links.sql"})
    @Test
    @Transactional
    @Rollback
    public void chatId_findLinksByChatId_emptyListOfLinks() {
        long chatId = chatRepository.findAll().get(0).getId();
        assertEquals(EXPECTED_EMPTY_TABLE_SIZE, linkChatRepository.findLinksByChatId(chatId).size());
    }
}
