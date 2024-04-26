package com.defi.kqxs;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;

public class LotteryProvider {
    public String code;
    public String name;
    public String region;
    public int[] drawingDays;
    int timestampStartDrawing;
    int timestampEndDrawing;
    public boolean isDrawing;

    JsonObject currentResult;

    public LotteryProvider(JsonObject json) {
        this.code = json.get("code").getAsString();
        this.name = json.get("name").getAsString();
        this.region = json.get("region").getAsString();
        this.drawingDays = GsonUtil.toIntArray(json.getAsJsonArray("day"));
        String hour = json.get("hour").getAsString();
        String[] params = hour.split(" - ");
        this.timestampStartDrawing = parseHourToTimeStamp(params[0]);
        this.timestampEndDrawing = parseHourToTimeStamp(params[1]);
        this.isDrawing = false;
    }

    private int parseHourToTimeStamp(String time) {
        String[] params = time.split("h");
        int hour = Integer.parseInt(params[0]);
        int minute = Integer.parseInt(params[1]);
        return (hour*3600 + minute*60);
    }

    public boolean checkTimeDrawing(){
        long now = System.currentTimeMillis()/1000;
        now = (now % 86400) + 25200;
        return now >= timestampEndDrawing;
    }

    public boolean checkToday(int today) {
        for(int i = 0; i < drawingDays.length; i++){
            if(drawingDays[i] == today){
                return true;
            }
        }
        return false;
    }
}
