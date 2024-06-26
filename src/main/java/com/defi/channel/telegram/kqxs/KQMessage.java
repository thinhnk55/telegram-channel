package com.defi.channel.telegram.kqxs;

import com.defi.channel.telegram.common.CommonMessage;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;

public class KQMessage {
    public static SendResponse draw_today(TelegramBot bot, long chat_id, String lang,
                                          String thu_ngay, String tinhMienTrung, String tinhMienNam){
        String content = KQLanguage.getEmojiMessage(lang, KQLanguage.draw_today);
        content = content.replace("@DAY", thu_ngay);
        content = content.replace("@TRUNG", tinhMienTrung);
        content = content.replace("@NAM", tinhMienNam);
        String footer = KQLanguage.getEmojiMessage(lang, KQLanguage.mkt_footer);
        content = content + footer;
        return CommonMessage.sendMessage(bot, chat_id, content);
    }
    public static SendResponse kqxs_mien_bac(TelegramBot bot, long chat_id, String lang, JsonObject data){
        String content = KQLanguage.getEmojiMessage(lang, KQLanguage.kq_mb);
        String thu = data.getAsJsonObject("time").get("thu").getAsString();
        String ngay = data.getAsJsonObject("time").get("ngay").getAsString();
        String day = new StringBuilder()
                .append(thu).append(" ")
                .append(ngay)
                .toString();
        content = content.replace("@DAY", day);
        String giaidb = data.getAsJsonObject("kq").get("giaidb").getAsString().replace("-", "\\-");
        content = content.replace("@GIAIDB", giaidb);
        String giai1 = data.getAsJsonObject("kq").get("giai1").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI1", giai1);
        String giai2 = data.getAsJsonObject("kq").get("giai2").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI2", giai2);
        String giai3 = data.getAsJsonObject("kq").get("giai3").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI3", giai3);
        String giai4 = data.getAsJsonObject("kq").get("giai4").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI4", giai4);
        String giai5 = data.getAsJsonObject("kq").get("giai5").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI5", giai5);
        String giai6 = data.getAsJsonObject("kq").get("giai6").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI6", giai6);
        String giai7 = data.getAsJsonObject("kq").get("giai7").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI7", giai7);

        String footer = KQLanguage.getEmojiMessage(lang, KQLanguage.mkt_footer);
        content = content + footer;
        return CommonMessage.sendMessage(bot, chat_id, content);
    }
    public static SendResponse kqxs_mien_trung(TelegramBot bot, long chat_id, String lang, String tinh, JsonObject data){
        String content = KQLanguage.getEmojiMessage(lang, KQLanguage.kq_mt);
        content = content.replace("@TINH", tinh);
        return kqxs_trung_nam(bot, chat_id, lang, data, content);
    }
    public static SendResponse kqxs_mien_nam(TelegramBot bot, long chat_id, String lang, String tinh, JsonObject data){
        String content = KQLanguage.getEmojiMessage(lang, KQLanguage.kq_mn);
        content = content.replace("@TINH", tinh);
        return kqxs_trung_nam(bot, chat_id, lang, data, content);
    }
    private static SendResponse kqxs_trung_nam(TelegramBot bot, long chat_id,
                                               String lang, JsonObject data, String content){
        String thu = data.getAsJsonObject("time").get("thu").getAsString();
        String ngay = data.getAsJsonObject("time").get("ngay").getAsString();
        String day = new StringBuilder()
                .append(thu).append(" ")
                .append(ngay)
                .toString();
        content = content.replace("@DAY", day);
        String giaidb = data.getAsJsonObject("kq").get("giaidb").getAsString().replace("-", "\\-");
        content = content.replace("@GIAIDB", giaidb);
        String giai1 = data.getAsJsonObject("kq").get("giai1").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI1", giai1);
        String giai2 = data.getAsJsonObject("kq").get("giai2").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI2", giai2);
        String giai3 = data.getAsJsonObject("kq").get("giai3").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI3", giai3);
        String giai4 = data.getAsJsonObject("kq").get("giai4").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI4", giai4);
        String giai5 = data.getAsJsonObject("kq").get("giai5").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI5", giai5);
        String giai6 = data.getAsJsonObject("kq").get("giai6").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI6", giai6);
        String giai7 = data.getAsJsonObject("kq").get("giai7").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI7", giai7);
        String giai8 = data.getAsJsonObject("kq").get("giai8").getAsString().replace("-", "\\-");
        content = content.replace("@GIAI8", giai8);

        String footer = KQLanguage.getEmojiMessage(lang, KQLanguage.mkt_footer);
        content = content + footer;
        return CommonMessage.sendMessage(bot, chat_id, content);
    }
}
