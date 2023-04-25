package ru.tinkoff.edu.java.bot.telegrambot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public interface TgBot extends UpdatesListener {
    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request);

    void start();
}
