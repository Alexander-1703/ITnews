package ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess;

import com.pengrad.telegrambot.request.SetMyCommands;

import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.HelpCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.ListCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.StartCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.TrackCommand;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.UntrackCommand;

public class CommandListBuilder {
    public static SetMyCommands buildMenu() {
        return new SetMyCommands(
                new StartCommand().toApiCommand(),
                new HelpCommand().toApiCommand(),
                new TrackCommand().toApiCommand(),
                new UntrackCommand().toApiCommand(),
                new ListCommand().toApiCommand()
        );
    }
}
