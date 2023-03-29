package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import java.util.concurrent.ThreadLocalRandom;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListCommand implements Command {
    @Override
    public String getCommand() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Получить список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("displaying a list of tracked links");
        return new SendMessage(update.message().chat().id(), getLinksList()).disableWebPagePreview(true);
    }

    private String getLinksList() {
        //mock
        if (ThreadLocalRandom.current().nextBoolean()) {
            return """
                    • https://github.com/Alexander-1703/ITnews
                    
                    • https://stackoverflow.com/questions/134/xsd-datasets-and-ignoring-foreign-keys
                    """;
        }
        return "Список отслеживаемых ссылок пустой!";
    }

}
