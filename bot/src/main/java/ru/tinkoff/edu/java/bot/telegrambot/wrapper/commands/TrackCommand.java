package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.enums.CommandEnum;
import ru.tinkoff.edu.java.bot.service.interfaces.LinkService;

import com.pengrad.telegrambot.model.request.ForceReply;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String TRACK_MESSAGE = """
            Ссылка добавлена в список отслеживания.
            Вы получите уведомление обо всех изменениях
            """;
    private static final String ERROR_MESSAGE = "Вы не прислали ссылку";
    private static final String REQUEST_LINK_TO_ADD = "Отправьте ссылку на ресурс, который хотите отслеживать.";
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
        Message message = update.message();

        if (message.replyToMessage() != null && message.replyToMessage().text().equals(REQUEST_LINK_TO_ADD)) {
            linkService.trackLink(chatId, message.text());
            return new SendMessage(chatId, TRACK_MESSAGE);
        }

        SendMessage requestMessage = new SendMessage(chatId, REQUEST_LINK_TO_ADD);
        requestMessage.replyMarkup(new ForceReply(true));
        return requestMessage;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        return message.text().startsWith("/") && getCommand().equals(message.text().split(" ")[0].substring(1)) ||
                message.replyToMessage() != null && message.replyToMessage().text().equals(REQUEST_LINK_TO_ADD);
    }
}
