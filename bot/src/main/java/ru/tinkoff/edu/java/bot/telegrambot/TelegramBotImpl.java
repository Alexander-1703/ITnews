package ru.tinkoff.edu.java.bot.telegrambot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.processor.CommandListBuilder;
import ru.tinkoff.edu.java.bot.telegrambot.wrapper.processor.UserMessageProcessor;

@Component
@Slf4j
public class TelegramBotImpl implements TgBot {
    private final TelegramBot bot;
    private final UserMessageProcessor userMessageProcessor;

    public TelegramBotImpl(@Value("${bot.token}") String token,
                           @Autowired UserMessageProcessor userMessageProcessor) {
        this.userMessageProcessor = userMessageProcessor;
        bot = new TelegramBot(token);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return bot.execute(request);
    }

    @Override
    @PostConstruct
    public void start() {
        setupUpdateListener();
        execute(CommandListBuilder.buildMenu());
    }

    private void setupUpdateListener() {
        bot.setUpdatesListener(updates -> {
            try {
                process(updates);
            } catch (RuntimeException e) {
                log.error("Error processing updates", e);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
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
