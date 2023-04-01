package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TrackCommand implements Command {
    private static final String TRACK_MESSAGE = """
            Ссылка добавлена в список отслеживания.
            Вы получите уведомление обо всех изменениях
            """;

    @Override
    public String getCommand() {
        return CommandEnum.TRACK.getCommandName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.TRACK.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("link tracking start...");
        return new SendMessage(update.message().chat().id(), TRACK_MESSAGE);
    }
}
