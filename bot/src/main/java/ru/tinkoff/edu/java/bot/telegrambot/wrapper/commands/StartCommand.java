package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.bot.enums.CommandEnum;
import ru.tinkoff.edu.java.bot.service.interfaces.ChatService;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String START_MESSAGE = """
        Привет!

        Я бот, который поможет тебе отслеживать обновления на популярных ресурсах,
        таких как github и stackoverflow.
        Ты можешь отправить ссылку на репозиторий github или на вопрос stackoverflow,
        и когда на этих ресурсах появятся обновления, я пришлю тебе уведомление!
        """;

    private final ChatService chatService;

    @Override
    public String getCommand() {
        return CommandEnum.START.getCommandName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.START.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        chatService.registerChat(chatId);
        return new SendMessage(chatId, START_MESSAGE);
    }
}
