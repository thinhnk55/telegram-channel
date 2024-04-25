package com.defi.kqxs;

import com.defi.common.SimpleResponse;
import com.defi.kqxs.service.IKqxsService;
import com.defi.kqxs.service.KqxsService;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.sqllite.SqlLiteManager;
import com.google.gson.JsonObject;

import java.util.Map;

public class KqxsManager {
    private static KqxsManager ins = null;

    private KqxsManager() {

    }

    public static KqxsManager instance() {
        if (ins == null) {
            ins = new KqxsManager();
        }
        return ins;
    }
    Map<String, LotteryProvider> lotteryProviders;
    public void init(String configFile, String table){
        service = new KqxsService(table, SqlLiteManager.instance().bridge);
    }

    IKqxsService service;

    public JsonObject getKQXS(String tinh, int day, int month, int year){
        String ngay = new StringBuilder()
                .append(day).append("/")
                .append(month).append("/")
                .append(year).toString();
        JsonObject response = service.getKqxs(tinh, ngay);
        if(!SimpleResponse.isSuccess(response)){
            DebugLogger.logger.info("{}", response);
            response = getKQXS(tinh, day, month, year);
            if(SimpleResponse.isSuccess(response)){
                JsonObject kq = response.getAsJsonObject("d");
                service.createKqxs(tinh, tinh, ngay, kq);
            }
        }
        return response;
    }
}
