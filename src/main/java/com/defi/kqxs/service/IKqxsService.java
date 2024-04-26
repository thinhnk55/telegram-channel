package com.defi.kqxs.service;

import com.google.gson.JsonObject;

public interface IKqxsService {
    JsonObject createKqxs(JsonObject kq);

    JsonObject getKqxs(String tinh, String ngay);
}
