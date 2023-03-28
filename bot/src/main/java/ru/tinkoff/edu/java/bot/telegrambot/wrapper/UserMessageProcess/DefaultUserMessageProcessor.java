package ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands.Command;

@Component
public class DefaultUserMessageProcessor implements UserMessageProcessor {
    private final List<? extends Command> commandsList;

    public DefaultUserMessageProcessor() {
        this.commandsList = CommandListBuilder.build();
    }

    @Override
    public List<? extends Command> commands() {
        return commandsList;
    }

    @Override
    public SendMessage process(Update update) {
        Message message = update.message();
        if (message != null && message.text() != null) {
            for (var command : commandsList) {
                if (command.supports(update)) {
                    return command.handle(update);
                }
            }
        }
        return null;
    }
}
