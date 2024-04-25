package com.defi.kqxs;

import com.google.gson.JsonObject;

public class LotteryProvider {
    String code;
    String name;
    int[] drawingDays;
    long timestampStartDrawing;
    long timestampEndDrawing;
    boolean isDrawing;

    JsonObject currentResult;

    public boolean checkTimeDrawing(){
        long now = System.currentTimeMillis();
        now = now % 86400000;
        return now >= timestampStartDrawing && now <= timestampEndDrawing;
    }
}
