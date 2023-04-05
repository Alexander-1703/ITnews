package ru.tinkoff.edu.java.bot.telegrambot.wrapper.processor;

import java.util.List;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.Command;

public interface UserMessageProcessor {
    List<Command> commands();

    SendMessage process(Update update);
}
