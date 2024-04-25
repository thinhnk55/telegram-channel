package com.defi.kqxs.service;

import com.google.gson.JsonObject;

public interface IKqxsService {
    JsonObject createKqxs(String tinh, String ten_tinh, String ngay, JsonObject kq);

    JsonObject getKqxs(String tinh, String ngay);
}
