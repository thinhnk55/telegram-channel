package com.defi.channel.telegram.common;

import com.defi.util.log.DebugLogger;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class CommonMessage {
    public static SendResponse sendMessage(TelegramBot bot, SendMessage message) {
        SendResponse response = bot.execute(message);
        if(!response.isOk()){
            DebugLogger.logger.info("Send Error {}", response.description());
        }
        return response;
    }
    public static SendResponse sendMessage(TelegramBot bot, long chat_id, String content) {
        SendMessage message = new SendMessage(chat_id, content)
            .parseMode(ParseMode.MarkdownV2);
        return sendMessage(bot, message);
    }
}

