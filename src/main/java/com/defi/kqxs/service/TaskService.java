package com.defi.kqxs.service;

import com.defi.common.SimpleResponse;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class TaskService implements ITaskService {
    String table;
    SQLJavaBridge bridge;
    public TaskService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
        boolean exist = bridge.checkTableExisting(table);
        if(!exist){
            createTable();
        }
    }
    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name ("
                + "task VARCHAR(256) PRIMARY KEY,"
                + "log VARCHAR(2048)"
                + ")").replace("table_name", table);
        bridge.createTable(table, createTableSQL);
    }
    @Override
    public JsonObject createTask(String task, JsonObject log) {
        try {
            String query = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(table)
                    .append(" (task, log) VALUES (?,?)")
                    .toString();
            int x = bridge.update(query, task, log);
            if (x == 1) {
                JsonObject json = new JsonObject();
                json.addProperty("task", task);
                json.add("log", log);
                return SimpleResponse.createResponse(0, json);
            }
            return SimpleResponse.createResponse(10);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject updateTask(String task, JsonObject log) {
        try {
            String query = new StringBuilder()
                    .append("UPDATE ")
                    .append(table)
                    .append(" SET log = ? WHERE task = ?")
                    .toString();
            bridge.update(query, log, task);
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject getTask(String task) {
        try {
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE task = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, task);
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
