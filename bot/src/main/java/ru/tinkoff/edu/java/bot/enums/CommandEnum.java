package ru.tinkoff.edu.java.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommandEnum {
    HELP("help", "Справка об использовании бота"),
    START("start", "Начало работы"),
    LIST("list", "Получить список отслеживаемых ссылок"),
    TRACK("track", "Начать отслеживание ссылки"),
    UNTRACK("untrack", "Прекратить отслеживание ссылки");

    @Getter
    private final String commandName;
    @Getter
    private final String description;
}
