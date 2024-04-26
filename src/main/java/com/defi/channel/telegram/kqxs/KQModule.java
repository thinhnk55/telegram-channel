package com.defi.channel.telegram.kqxs;

import com.defi.channel.telegram.BaseModule;
import com.defi.channel.telegram.common.TelegramUtil;
import com.defi.common.SimpleResponse;
import com.defi.kqxs.KQXSHelper;
import com.defi.kqxs.KqxsManager;
import com.defi.kqxs.LotteryProvider;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;

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
        JsonObject response = KQXSHelper.getKQXS("mien-bac", 24,4,2024);
        if(SimpleResponse.isSuccess(response)){
            JsonObject kq = response.getAsJsonObject("d");
            SendResponse sendResponse =
                    KQMessage.kqxs_mien_bac(bot, TelegramUtil.getChatId(update), TelegramUtil.getLanguage(update), kq);
            if(sendResponse.isOk()){
                DebugLogger.logger.info("{} {}", sendResponse.message().chat().id(),
                        sendResponse.message().messageId());
            }
        }
        response = KQXSHelper.getKQXS("vinh-long", 26,4,2024);
        if(SimpleResponse.isSuccess(response)){
            JsonObject kq = response.getAsJsonObject("d");
            LotteryProvider provider = KqxsManager.instance().lotteryProviders.get("vinh-long");
            SendResponse sendResponse =
                    KQMessage.kqxs_mien_nam(bot, TelegramUtil.getChatId(update), TelegramUtil.getLanguage(update), provider.name, kq);
            if(sendResponse.isOk()){
                DebugLogger.logger.info("{} {}", sendResponse.message().chat().id(),
                        sendResponse.message().messageId());
            }
        }
        response = KQXSHelper.getKQXS("gia-lai", 26,4,2024);
        if(SimpleResponse.isSuccess(response)){
            JsonObject kq = response.getAsJsonObject("d");
            LotteryProvider provider = KqxsManager.instance().lotteryProviders.get("gia-lai");
            SendResponse sendResponse =
                    KQMessage.kqxs_mien_trung(bot, TelegramUtil.getChatId(update), TelegramUtil.getLanguage(update), provider.name, kq);
            if(sendResponse.isOk()){
                DebugLogger.logger.info("{} {}", sendResponse.message().chat().id(),
                        sendResponse.message().messageId());
            }
        }
    }
}
