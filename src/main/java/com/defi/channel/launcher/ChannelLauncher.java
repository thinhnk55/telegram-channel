package com.defi.channel.launcher;

import com.defi.channel.telegram.TelegramChannelManager;
import com.defi.kqxs.KqxsManager;
import com.defi.util.sql.sqllite.SqlLiteManager;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChannelLauncher {
    public static void main(String[] args) {
        try {
            initConfig();
            initLogic();
            startLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startLoop() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(ChannelLauncher::loop, 1, 1, TimeUnit.MINUTES);
    }

    private static void loop() {
        TelegramChannelManager.instance().loop();
    }

    private static void initLogic() {
        KqxsManager.instance().init("kqxs");
        TelegramChannelManager.instance().init("data/telegram/channel_bot.json");
    }

    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/channel/log/log4j.xml");
        SqlLiteManager.instance().init("config/channel/sql/sqllite.json",
                "config/channel/sql/hikari.properties");
    }
}
