package com.defi.kqxs.service;

import com.defi.common.SimpleResponse;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class KqxsService implements IKqxsService{
    String table;
    SQLJavaBridge bridge;
    public KqxsService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
        boolean exist = bridge.checkTableExisting(table);
        if(!exist){
            createTable();
        }
    }
    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name ("
                + "tinh VARCHAR(32),"
                + "ngay VARCHAR(32),"
                + "kq VARCHAR(2048),"
                + "PRIMARY KEY(tinh, ngay)"
                + ")").replace("table_name", table);
        bridge.createTable(table, createTableSQL);
    }
    @Override
    public JsonObject createKqxs(JsonObject data) {
        try {
            String tinh = data.get("tinh").getAsString();
            String ngay = data.get("ngay").getAsString();
            JsonObject kq = data.getAsJsonObject("kq");
            String query = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(table)
                    .append(" (tinh, ngay, kq) VALUES (?,?,?)")
                    .toString();
            int x = bridge.update(query, tinh, ngay, kq);
            if (x == 1) {
                return SimpleResponse.createResponse(0, kq);
            }
            return SimpleResponse.createResponse(10);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject getKqxs(String tinh, String ngay) {
        try {
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE tinh = ? AND ngay = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, tinh, ngay);
            if(json == null){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
}
