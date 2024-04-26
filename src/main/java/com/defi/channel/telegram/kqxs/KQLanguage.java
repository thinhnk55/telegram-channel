package com.defi.channel.telegram.kqxs;

import com.defi.channel.telegram.LanguageMessage;

public class KQLanguage {
    public static final String kq_mb = "kq_mb";
    public static final String kq_mt = "kq_mt";
    public static final String kq_mn = "kq_mn";
    public static final String draw_today = "draw_today";
    public static final String mkt_footer = "mkt_footer";
    public static LanguageMessage message;
    public static void init(String configFile){
        message = new LanguageMessage(configFile);
    }
    public static String getEmojiMessage(String lang, String key){
        return message.getEmojiMessage(lang, key);
    }
}
