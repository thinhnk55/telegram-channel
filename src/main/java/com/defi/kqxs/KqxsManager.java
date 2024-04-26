package com.defi.kqxs;

import com.defi.channel.telegram.TelegramChannelManager;
import com.defi.channel.telegram.kqxs.KQMessage;
import com.defi.common.SimpleResponse;
import com.defi.kqxs.service.IKqxsService;
import com.defi.kqxs.service.ITaskService;
import com.defi.kqxs.service.KqxsService;
import com.defi.kqxs.service.TaskService;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.sqllite.SqlLiteManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.*;

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
    public Map<String, LotteryProvider> lotteryProviders;
    Map<String, JsonObject> cachedTask;
    String[] day_in_week;
    public void init(String configFile){
        lotteryProviders = new HashMap<>();
        cachedTask = new HashMap<>();
        JsonObject config = GsonUtil.getJsonObject(configFile);
        JsonArray providers = config.getAsJsonArray("tinh");
        for(int i = 0; i < providers.size(); i++){
            JsonObject json = providers.get(i).getAsJsonObject();
            LotteryProvider provider = new LotteryProvider(json);
            lotteryProviders.put(provider.code, provider);
        }
        String kqxs_table = config.get("kqxs_table").getAsString();
        kqxsService = new KqxsService(kqxs_table, SqlLiteManager.instance().bridge);
        String task_table = config.get("task_table").getAsString();
        taskService = new TaskService(task_table, SqlLiteManager.instance().bridge);
        day_in_week = GsonUtil.toStringArray(config.getAsJsonArray("day_in_week"));
    }

    public IKqxsService kqxsService;
    public ITaskService taskService;

    public JsonObject getKQXS(String tinh, int day, int month, int year){
        String ngay = convertNgay(day, month, year);
        JsonObject response = kqxsService.getKqxs(tinh, ngay);
        if(!SimpleResponse.isSuccess(response)){
            response = KQXSHelper.getKQXS(tinh, day, month, year);
            if(SimpleResponse.isSuccess(response)){
                JsonObject kq = response.getAsJsonObject("d");
                response = kqxsService.createKqxs(kq);
                if(SimpleResponse.isSuccess(response)){
                    return response;
                }else{
                    DebugLogger.logger.info("{}", response);
                    return SimpleResponse.createResponse(11);
                }
            }else{
                DebugLogger.logger.info("{}", response);
                return SimpleResponse.createResponse(10);
            }
        }
        return response;
    }

    public void loop() {
        task();
    }

    private void task() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        String ngay = convertNgay(day, month, year);
        boolean isDone = checkRemindTask(ngay);
        if(!isDone){
            remindTask(ngay, today);
        }
        result(today, day, month, year, ngay);
    }

    private String convertNgay(int day, int month, int year) {
        String ngay = new StringBuilder()
                .append(day < 10 ? "0":"")
                .append(day).append("/")
                .append(month < 10 ? "0":"")
                .append(month).append("/")
                .append(year).toString();
        return ngay;
    }

    private void result(int today, int day, int month, int year, String ngay) {
        for(LotteryProvider provider: lotteryProviders.values()){
            if(provider.checkToday(today) && provider.checkTimeDrawing()){
                boolean isDone = checkResultTask(provider.code, ngay);
                if(!isDone) {
                    JsonObject response = KQXSHelper.getKQXS(provider.code, day, month, year);
                    if (SimpleResponse.isSuccess(response)) {
                        TelegramBot bot = TelegramChannelManager.instance().getTelegramBot();
                        long chat_id = TelegramChannelManager.instance().channel_id;
                        if (provider.region.equals("bac")) {
                            JsonObject data = response.getAsJsonObject("d");
                            String ngayThucTe = data.getAsJsonObject("time").get("ngay").getAsString();
                            if(ngayThucTe.equals(ngay)) {
                                SendResponse sendResponse = KQMessage.kqxs_mien_bac(bot, chat_id, "vi", data);
                                if (sendResponse.isOk()) {
                                    completeResultTask(provider.code, ngay, chat_id, sendResponse);
                                }
                            }
                        }
                        if (provider.region.equals("trung")) {
                            JsonObject data = response.getAsJsonObject("d");
                            String ngayThucTe = data.getAsJsonObject("time").get("ngay").getAsString();
                            if(ngayThucTe.equals(ngay)) {
                                SendResponse sendResponse = KQMessage.kqxs_mien_trung(bot, chat_id, "vi", provider.name, data);
                                if (sendResponse.isOk()) {
                                    completeResultTask(provider.code, ngay, chat_id, sendResponse);
                                }
                            }
                        }
                        if (provider.region.equals("nam")) {
                            JsonObject data = response.getAsJsonObject("d");
                            String ngayThucTe = data.getAsJsonObject("time").get("ngay").getAsString();
                            if(ngayThucTe.equals(ngay)) {
                                SendResponse sendResponse = KQMessage.kqxs_mien_nam(bot, chat_id, "vi", provider.name, data);
                                if (sendResponse.isOk()) {
                                    completeResultTask(provider.code, ngay, chat_id, sendResponse);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void completeResultTask(String code, String ngay, long chat_id, SendResponse sendResponse) {
        String task = new StringBuilder()
                .append("result-")
                .append(code).append("-")
                .append(ngay).toString();
        JsonObject json = new JsonObject();
        json.addProperty("chat_id", chat_id);
        json.addProperty("message_id", sendResponse.message().messageId());
        taskService.createTask(task, json);
    }

    private boolean checkResultTask(String code, String ngay) {
        String task = new StringBuilder()
                .append("result-")
                .append(code).append("-")
                .append(ngay).toString();
        JsonObject response = taskService.getTask(task);
        return SimpleResponse.isSuccess(response);
    }

    private void remindTask(String ngay, int today) {
        String thu_ngay = convertThuNgay(today, ngay);
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for(LotteryProvider provider: lotteryProviders.values()){
            if(provider.checkToday(today)){
                if(provider.region.equals("trung")){
                    sb1.append(provider.name).append(", ");
                }
                if(provider.region.equals("nam")){
                    sb2.append(provider.name).append(", ");
                }
            }
        }
        if(sb1.length() > 0) {
            sb1.delete(sb1.length() - 2, sb1.length() );
        }
        if(sb2.length() > 0) {
            sb2.delete(sb2.length() - 2, sb2.length());
        }
        String tinhMienTrung = sb1.toString();
        String tinhMienNam = sb2.toString();
        TelegramBot bot = TelegramChannelManager.instance().getTelegramBot();
        long chat_id = TelegramChannelManager.instance().channel_id;
        SendResponse response = KQMessage.draw_today(bot, chat_id, "vi", thu_ngay, tinhMienTrung, tinhMienNam);
        if(response.isOk()) {
            JsonObject json = new JsonObject();
            json.addProperty("chat_id", chat_id);
            json.addProperty("message_id", response.message().messageId());
            String task = new StringBuilder()
                    .append("remind-")
                    .append(ngay).toString();
            taskService.createTask(task, json);
        }
    }

    private String convertThuNgay(int today, String ngay) {
        return new StringBuilder()
                .append(day_in_week[today])
                .append(" ").append(ngay).toString();
    }

    private boolean checkRemindTask(String ngay) {
        String task = new StringBuilder()
                .append("remind-")
                .append(ngay).toString();
        JsonObject response = taskService.getTask(task);
        return SimpleResponse.isSuccess(response);
    }
}
