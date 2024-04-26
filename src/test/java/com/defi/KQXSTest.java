package com.defi;

import com.defi.common.SimpleResponse;
import com.defi.kqxs.KQXSHelper;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.Calendar;

public class KQXSTest {
    public static void main(String[] args) {
        try {
            initConfig();
            JsonObject config = GsonUtil.getJsonObject("data/kqxs/kqxs.json");
            JsonArray array = config.getAsJsonArray("tinh");
            for(int i = 0; i < array.size(); i++) {
                JsonObject tinhJson = array.get(i).getAsJsonObject();
                String tinh = tinhJson.get("code").getAsString();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -8);
                JsonArray days = tinhJson.getAsJsonArray("day");
                for(int j = 0; j < days.size(); j++) {
                    int thu = days.get(j).getAsInt();
                    calendar.set(Calendar.DAY_OF_WEEK, thu);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    JsonObject response = KQXSHelper.getKQXS(tinh, day, month, year);
                    if(SimpleResponse.isSuccess(response)){
                        JsonObject kq = response.getAsJsonObject("d");
                        DebugLogger.logger.info(kq.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void initConfig(){
        DOMConfigurator.configure("config/channel/log/log4j.xml");
    }
}
