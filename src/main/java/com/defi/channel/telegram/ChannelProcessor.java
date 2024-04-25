package com.defi.channel.telegram;

import com.defi.channel.telegram.kqxs.KQModule;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.LinkedList;
import java.util.List;

public class ChannelProcessor implements ITelegramProcessor{
    JsonObject config;
    List<BaseModule> modules;


    public ChannelProcessor(JsonObject config){
        this.config = config;
        modules = new LinkedList<>();
        JsonObject kqConfig = config.getAsJsonObject("modules").getAsJsonObject("kqxs");
        KQModule kqModule = new KQModule(kqConfig);
        modules.add(kqModule);
    }
    @Override
    public void process(TelegramBot bot, Update update) {
        try {
            DebugLogger.logger.info("{}", GsonUtil.gson.toJson(update));
            for(BaseModule module: modules){
                if(module.accept(update)){
                    module.process(bot, update);
                    break;
                }
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
