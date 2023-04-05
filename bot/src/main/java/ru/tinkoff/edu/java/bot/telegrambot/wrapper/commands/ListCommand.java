package ru.tinkoff.edu.java.bot.telegrambot.wrapper.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.enums.CommandEnum;
import ru.tinkoff.edu.java.bot.service.LinkService;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ListCommand implements Command {
    private static final String EMPTY_LIST_MESSAGE = "Список отслеживаемых ссылок пустой!";
    private LinkService linkService;

    @Autowired
    public void setLinkService(LinkService linkService) {
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
        List<LinkResponse> linkList = linkService.getLinks(chatId);
        if (linkList.isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }
        return linkList.stream()
                .map(item -> item.uri().toString())
                .collect(Collectors.joining("\n"));
    }

}
