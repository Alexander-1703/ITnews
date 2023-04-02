package ru.tinkoff.edu.java.bot.commands;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess.DefaultUserMessageProcessor;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.HelpCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.ListCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.StartCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.TrackCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.UntrackCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UnknownCommandTest {
    private static final String UNKNOWN_COMMAND_MESSAGE = "Такой команды не существует!";

    @Test
    public void unknownCommandTest() {
        //given
        long chatId = 0L;
        DefaultUserMessageProcessor userMessageProcessor = new DefaultUserMessageProcessor(List.of(
                new HelpCommand(),
                new StartCommand(),
                new TrackCommand(),
                new UntrackCommand(),
                new ListCommand()
        ));
        Update update = getUpdate(chatId);

        //when
        SendMessage message = userMessageProcessor.process(update);

        //then
        assertEquals(message.getParameters().get("text"), UNKNOWN_COMMAND_MESSAGE);
    }

    private Update getUpdate(long chatId) {
        Update updateMock = Mockito.mock(Update.class);
        Message messageMock = Mockito.mock(Message.class);
        Chat chatMock = Mockito.mock(Chat.class);
        when(updateMock.message()).thenReturn(messageMock);
        when(messageMock.chat()).thenReturn(chatMock);
        when(chatMock.id()).thenReturn(chatId);
        when(messageMock.text()).thenReturn("/unknown command");
        return updateMock;
    }
}
