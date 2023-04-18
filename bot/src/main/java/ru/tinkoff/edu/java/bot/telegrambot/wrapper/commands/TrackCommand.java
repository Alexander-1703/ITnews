package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.enums.CommandEnum;
import ru.tinkoff.edu.java.bot.service.interfaces.LinkService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String TRACK_MESSAGE = """
            Ссылка добавлена в список отслеживания.
            Вы получите уведомление обо всех изменениях
            """;
    private final LinkService linkService;

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
        long chatId = update.message().chat().id();
        String link = update.message().text().split(" ")[1];
        linkService.trackLink(chatId, link);
        return new SendMessage(chatId, TRACK_MESSAGE);
    }
}
