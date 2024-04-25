package com.defi.channel.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface ITelegramProcessor {
    public void process(TelegramBot bot, Update update);
}
