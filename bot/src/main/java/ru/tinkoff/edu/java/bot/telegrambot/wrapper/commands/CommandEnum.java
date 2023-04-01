package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommandEnum {
    HELP("help", "Справка об использовании бота"),
    START("start", "Получить список отслеживаемых ссылок"),
    LIST("list", "Начало работы"),
    TRACK("track", "Начать отслеживание ссылки"),
    UNTRACK("untrack", "Прекратить отслеживание ссылки");

    @Getter
    private final String commandName;
    @Getter
    private final String description;
}
