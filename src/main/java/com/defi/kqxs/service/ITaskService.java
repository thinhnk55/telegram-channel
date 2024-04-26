package com.defi.kqxs.service;

import com.google.gson.JsonObject;

public interface ITaskService {
    JsonObject createTask(String task, JsonObject log);
    JsonObject updateTask(String task, JsonObject log);
    JsonObject getTask(String task);
}
