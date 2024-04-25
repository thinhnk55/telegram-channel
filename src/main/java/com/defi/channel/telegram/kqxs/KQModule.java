package com.defi.channel.telegram.kqxs;

import com.defi.channel.telegram.BaseModule;
import com.defi.channel.telegram.common.TelegramUtil;
import com.defi.common.SimpleResponse;
import com.defi.kqxs.KQXS;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public class KQModule implements BaseModule {
    JsonObject config;
    public KQModule(JsonObject config){
        this.config = config;
        String language_file = config.get("lang").getAsString();
        KQLanguage.init(language_file);
    }

    @Override
    public boolean accept(Update update) {
        return true;
    }

    @Override
    public void process(TelegramBot bot, Update update) {
        JsonObject response = KQXS.getKQXS("mien-bac", 24,4,2024);
        if(SimpleResponse.isSuccess(response)){
            JsonObject kq = response.getAsJsonObject("d");
            KQMessage.kqxs_mienbac(bot, TelegramUtil.getChatId(update), TelegramUtil.getLanguage(update), kq);
        }
    }
}
