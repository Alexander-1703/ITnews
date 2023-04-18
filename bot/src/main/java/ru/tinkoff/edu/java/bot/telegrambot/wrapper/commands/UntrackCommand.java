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
public class UntrackCommand implements Command {
    private static final String UNTRACK_MESSAGE = """
            Ссылка удалена из списка отслеживания.
            Вы больше не будете получать уведомления об изменениях
            """;
    private final LinkService linkService;

    @Override
    public String getCommand() {
        return CommandEnum.UNTRACK.getCommandName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.UNTRACK.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("link tracking end...");
        long chatId = update.message().chat().id();
        String link = update.message().text().split(" ")[1];
        linkService.untrackLink(chatId, link);
        return new SendMessage(chatId, UNTRACK_MESSAGE);
    }
}
