package com.defi.kqxs;

import com.defi.common.SimpleResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class KQXS {
    public static JsonObject getKQXS(String name, int day, int month, int year){
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        String url = new StringBuilder()
                .append("https://www.xoso.net/getkqxs/")
                .append(name).append("/")
                .append(day).append("-")
                .append(month).append("-")
                .append(year).append(".js")
                .toString();
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            Element element = document.getElementsByClass("content").first();
            if(element == null){
                return SimpleResponse.createResponse(10);
            }
            String thu = element.getElementsByClass("thu").first().text();
            String ngay = "";
            if(name.equals("mien-bac") || name.equals("binhdinh")
                    || name.equals("da-nang") || name.equals("dak-lak")
                    || name.equals("dak-nong") || name.equals("gia-lai")
                    || name.equals("khanh-hoa") || name.equals("kon-tum")
                    || name.equals("ninh-thuan") || name.equals("phu-yen")
                    || name.equals("quang-binh") || name.equals("quang-nam")
                    || name.equals("quang-ngai") || name.equals("quang-tri")
                    || name.equals("thua-thien-hue")
            ) {
                ngay = element.getElementsByClass("ngay").first().text();
                String[] subs = ngay.split(" ");
                ngay = subs[subs.length-1];
            }else{
                ngay = document.getElementsByClass("title").first().text();
                String[] subs = ngay.split(" ");
                ngay = subs[subs.length-1];
            }
            JsonObject time = new JsonObject();
            json.add("time", time);
            time.addProperty("thu", thu);
            time.addProperty("ngay", ngay);
            JsonObject kq = new JsonObject();
            json.add("kq", kq);
            JsonObject extra = new JsonObject();
            String giaidb = element.getElementsByClass("giaidb").first().text();
            kq.addProperty("giaidb", giaidb);
            String giai1 = element.getElementsByClass("giai1").first().text();
            kq.addProperty("giai1", giai1);
            String giai2 = element.getElementsByClass("giai2").first().text();
            kq.addProperty("giai2", giai2);
            String giai3 = element.getElementsByClass("giai3").first().text();
            kq.addProperty("giai3", giai3);
            String giai4 = element.getElementsByClass("giai3").first().text();
            kq.addProperty("giai4", giai4);
            String giai5 = element.getElementsByClass("giai5").first().text();
            kq.addProperty("giai5", giai5);
            String giai6 = element.getElementsByClass("giai6").first().text();
            kq.addProperty("giai6", giai6);
            String giai7 = element.getElementsByClass("giai7").first().text();
            kq.addProperty("giai7", giai7);
            Element giai8E = element.getElementsByClass("giai8").first();
            if(giai8E != null) {
                String giai8 = element.getElementsByClass("giai8").first().text();
                kq.addProperty("giai8", giai8);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            return SimpleResponse.createResponse(1);
        }
    }
    public static JsonObject calculateLoDe(JsonObject data) {
        JsonObject lode = new JsonObject();
        JsonArray lo = new JsonArray();
        lode.add("lo", lo);
        JsonObject kq = data.getAsJsonObject("kq");
        String giaidb = kq.get("giaidb").getAsString();
        int de = Integer.parseInt(giaidb.substring(giaidb.length()-3));
        lode.addProperty("de",de);
        addLo(giaidb, lo);
        addLo(kq.get("giai1").getAsString(), lo);
        addLo(kq.get("giai2").getAsString(), lo);
        addLo(kq.get("giai3").getAsString(), lo);
        addLo(kq.get("giai4").getAsString(), lo);
        addLo(kq.get("giai5").getAsString(), lo);
        addLo(kq.get("giai6").getAsString(), lo);
        addLo(kq.get("giai7").getAsString(), lo);
        if(kq.has("giai8")){
            addLo(kq.get("giai8").getAsString(), lo);
        }
        return lode;
    }

    private static void addLo(String giai, JsonArray array) {
        String[] params = giai.split(" - ");
        for(int i = 0; i < params.length; i++){
            String text = params[i];
            int lo = Integer.parseInt(text.substring(text.length()-2));
            array.add(lo);
        }
    }

}
