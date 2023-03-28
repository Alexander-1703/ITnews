package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String getCommand();

    String getDescription();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        String messageText = update.message().text();
        return messageText.startsWith("/") && getCommand().equals(messageText.split(" ")[0]);
    }

    default BotCommand toApiCommand() {
        return new BotCommand(getCommand(), getDescription());
    }
}
