package com.defi.channel.telegram;

import com.defi.channel.telegram.common.CommonMessage;
import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;

public class TelegramChannelManager {
    private static TelegramChannelManager ins;
    public static TelegramChannelManager instance() {
        if(ins == null){
            ins = new TelegramChannelManager();
        }
        return ins;
    }
    public TelegramChannelManager(){

    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String name = config.get("name").getAsString();
        String token = config.get("token").getAsString();
        channel_id = config.get("channel_id").getAsLong();
        ChannelProcessor channelProcessor = new ChannelProcessor(config);
        channelBot = new BaseBot(channelProcessor, name, token);
        channelBot.run();
    }
    BaseBot channelBot;
    long channel_id;

    public void sendToChannel(String content){
        CommonMessage.sendMessage(channelBot.bot, channel_id, content);
    }

    public void loop() {

    }
}
