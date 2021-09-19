package com.example.datastorage.service;

import com.example.datastorage.model.DataModel;

import java.util.Map;

public interface DataStorageService {
    Map<String, DataModel> getAll();

    DataModel get(String key);

    void set(String key, Object data, long ttl);

    DataModel remove(String key);

    String dump();

    void load(String data);
}
