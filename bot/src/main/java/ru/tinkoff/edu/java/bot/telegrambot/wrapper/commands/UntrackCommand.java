package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
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
    private static final String REQUEST_LINK_TO_REMOVE =
            "Отправьте ссылку на ресурс, который больше не хотите отслеживать.";
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
        Message message = update.message();

        if (message.replyToMessage() != null && message.replyToMessage().text().equals(REQUEST_LINK_TO_REMOVE)) {
            linkService.untrackLink(chatId, message.text());
            return new SendMessage(chatId, UNTRACK_MESSAGE);
        }

        SendMessage requestMessage = new SendMessage(chatId, REQUEST_LINK_TO_REMOVE);
        requestMessage.replyMarkup(new ForceReply(true));
        return requestMessage;
    }

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        return message.text().startsWith("/") && getCommand().equals(message.text().split(" ")[0].substring(1)) ||
                message.replyToMessage() != null && message.replyToMessage().text().equals(REQUEST_LINK_TO_REMOVE);
    }
}
