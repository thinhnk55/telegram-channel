package com.defi;

import com.defi.kqxs.KqxsManager;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.sqllite.SqlLiteManager;
import com.google.gson.JsonObject;
import org.apache.log4j.xml.DOMConfigurator;

public class SQLLiteTest {
    public static void main(String[] args) {
        try {
            initConfig();
            testSQLLite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testSQLLite() {
        KqxsManager.instance().init("data/kqxs/kqxs.json");
        int id = 1;
        JsonObject data = new JsonObject();
        data.addProperty("id", id);
        String task = "task-" + id;
        JsonObject response = KqxsManager.instance().taskService.createTask(task, data);
        DebugLogger.logger.info("{}",response);
        response = KqxsManager.instance().taskService.getTask(task);
        DebugLogger.logger.info("{}",response);
    }

    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/channel/log/log4j.xml");
        SqlLiteManager.instance().init("config/channel/sql/sqllite.json",
                "config/channel/sql/hikari.properties");
    }
}
