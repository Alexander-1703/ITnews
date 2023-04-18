package ru.tinkoff.edu.java.bot.commands;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.interfaces.LinkService;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.ListCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CommandTest {
    private static final String EMPTY_LIST_MESSAGE = "Список отслеживаемых ссылок пустой!";

    private LinkService linkService;
    private ListCommand listCommand;

    @BeforeEach
    public void setup() {
        listCommand = new ListCommand();
        linkService = Mockito.mock(LinkService.class);
        listCommand.setLinkService(linkService);
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
        assertEquals(linkList.stream().map(item -> item.uri().toString()).collect(Collectors.joining("\n")),
                message.getParameters().get("text"));
    }

    @Test
    public void emptyListTest() {
        //given
        long chatId = 0L;
        Update update = getUpdate(chatId);
        when(linkService.getLinks(chatId)).thenReturn(List.of());

        //when
        SendMessage message = listCommand.handle(update);

        //then
        assertEquals(EMPTY_LIST_MESSAGE, message.getParameters().get("text"));
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
}
