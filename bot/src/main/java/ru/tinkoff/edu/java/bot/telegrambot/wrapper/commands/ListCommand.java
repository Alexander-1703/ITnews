package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;

@Slf4j
@Component
public class ListCommand implements Command {
    private LinkService linkService;

    @Autowired
    public ListCommand(LinkService linkService) {
        this.linkService = linkService;
    }

    @Override
    public String getCommand() {
        return CommandEnum.LIST.getCommandName();
    }

    @Override
    public String getDescription() {
        return CommandEnum.LIST.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        log.info("displaying a list of tracked links");
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, formatLinks(chatId)).disableWebPagePreview(true);
    }

    private String formatLinks(long chatId) {
        List<LinkResponse> links = linkService.getLinks(chatId);
        StringBuilder sb = new StringBuilder();
        for (LinkResponse link : links) {
            sb.append(link).append("\n");
        }
        return sb.toString();
    }

}
