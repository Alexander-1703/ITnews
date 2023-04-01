package ru.tinkoff.edu.java.bot.telegrambot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.TgBot;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess.CommandListBuilder;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.UserMessageProcess.UserMessageProcessor;

@Component
@Slf4j
public class TelegramBotImpl implements TgBot {
    private final TelegramBot bot;
    private final UserMessageProcessor userMessageProcessor;
    private int lastUpdatedId = 0;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           @Autowired UserMessageProcessor userMessageProcessor) {
        this.userMessageProcessor = userMessageProcessor;
        bot = new TelegramBot(token);
        execute(CommandListBuilder.buildMenu());
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return bot.execute(request);
    }

    @Override
    public void start() {
        //TODO: подключение к бд
    }

    @Override
    public void fetchUpdates() {
        GetUpdates getUpdates = new GetUpdates().limit(100).offset(lastUpdatedId + 1);
        GetUpdatesResponse updatesResponse = execute(getUpdates);
        List<Update> updates = updatesResponse.updates();

        if (!updates.isEmpty()) {
            lastUpdatedId = updates.get(updates.size() - 1).updateId();
            process(updates);
        }
    }

    @Override
    public int process(List<Update> updates) {
        int processedUpdates = 0;
        for (Update update : updates) {
            SendMessage response = userMessageProcessor.process(update);
            if (response != null) {
                response.parseMode(ParseMode.Markdown);
                log.info("Sending response");
                execute(response);
            }
            processedUpdates++;
        }
        return processedUpdates;
    }

    @Override
    public void close() {
        //TODO: закрытие соединения с бд
    }
}
