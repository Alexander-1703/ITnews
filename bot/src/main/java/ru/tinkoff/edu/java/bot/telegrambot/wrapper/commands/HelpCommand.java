package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.enums.CommandEnum;

@Component
public class HelpCommand implements Command {
    private static final String HELP_MESSAGE = """
            Для того, чтобы начать отслеживать ресурс достаточно отправить мне ссылку на репозиторий github или 
            на вопрос stackoverflow, и когда на этих ресурсах появятся обновления, я пришлю тебе уведомление!
                
            *Справка по командам:*
                
            /start - зарегистрировать пользователя
            /help - вывести окно с командами
            /track - начать отслеживание ссылки
            /untrack - прекратить отслеживание ссылки
            /list - показать список отслеживаемых ссылок
            """;

    @Override
    public String getCommand() {
        return CommandEnum.HELP.getCommandName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.HELP.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), HELP_MESSAGE);
    }
}
