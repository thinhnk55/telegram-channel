package com.defi.channel.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface BaseModule {
    boolean accept(Update update);
    void process(TelegramBot bot, Update update);
}
