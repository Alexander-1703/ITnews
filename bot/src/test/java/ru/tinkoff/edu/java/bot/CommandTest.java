package ru.tinkoff.edu.java.bot;


import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.Command;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.ListCommand;

import static org.mockito.Mockito.when;

public class CommandTest {
    @Mock
    LinkService linkService;
    Command listCommand;

    @BeforeAll
    void setup() {
        listCommand = new ListCommand();
    }

    private Update getUpdate(long chatId) {
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        when(updateMock.message()).thenReturn(messageMock);
        when(messageMock.chat()).thenReturn(chatMock);
        when(chatMock.id()).thenReturn(chatId);
        when(messageMock.text()).thenReturn("/list");
        return updateMock;
    }

    @Test
    public void notEmptyListTest() {
        //given
        long chatId = 0L;
        List<LinkResponse> linkList = List.of(
                new LinkResponse(chatId, URI.create("github.com/Alexander-1703/ITnews")),
                new LinkResponse(chatId, URI.create("stackoverflow.com/questions/123")));
        Update update = getUpdate(chatId);

        when(linkService.getLinks(chatId)).thenReturn(linkList);

        //when
        SendMessage message = listCommand.handle(update);

        //then
    }
}
