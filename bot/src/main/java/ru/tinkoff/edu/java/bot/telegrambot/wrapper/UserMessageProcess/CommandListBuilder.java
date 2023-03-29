package ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess;

import java.util.Arrays;
import java.util.List;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;

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
                new ListCommand()
        );
    }

    public static SetMyCommands buildMenu() {
        Command start = new StartCommand();
        Command help = new HelpCommand();
        Command track = new TrackCommand();
        Command untrack = new UntrackCommand();
        Command list = new ListCommand();

        return new SetMyCommands(
                new BotCommand(start.getCommand(), start.getDescription()),
                new BotCommand(help.getCommand(), help.getDescription()),
                new BotCommand(track.getCommand(), track.getDescription()),
                new BotCommand(untrack.getCommand(), untrack.getDescription()),
                new BotCommand(list.getCommand(), list.getDescription())
        );
    }
}
