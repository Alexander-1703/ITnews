package ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.Command;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.HelpCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.ListCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.StartCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.TrackCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.UntrackCommand;

public class CommandListBuilder {
    public static List<? extends Command> build() {
        return Arrays.asList(
                new StartCommand(),
                new HelpCommand(),
                new TrackCommand(),
                new UntrackCommand(),
                new ListCommand());
    }
}
