package com.example.datastorage.service.impl;

import com.example.datastorage.model.DataModel;
import com.example.datastorage.service.DataStorageService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataStorageServiceImpl implements DataStorageService {
    
    private Map<String, DataModel> database = new HashMap<>();

    private boolean isDead(DataModel data) {
        return System.currentTimeMillis() > data.getTimeToDelete();
    }

    private void removeAllDeadObjects() {
        if (!database.isEmpty())
            database.entrySet()
                    .removeIf(entry -> isDead(entry.getValue()));
    }

    @Override
    public Map<String, DataModel> getAll() {
        removeAllDeadObjects();
        return database;
    }

    @Override
    public DataModel get(String key) {
        DataModel data = database.get(key);
        if (data == null)
            return null;
        if (isDead(data)) {
            database.remove(key);
            return null;
        }
        return database.get(key);
    }

    @Override
    public void set(String key, Object data, long ttl) {
        database.put(key, new DataModel(data, System.currentTimeMillis() + ttl));
    }

    @Override
    public DataModel remove(String key) {
        return database.remove(key);
    }

    @Override
    public String dump() {
        removeAllDeadObjects();
        return database.entrySet()
                .stream()
                .map(this::convertEntryToString)
                .collect(Collectors.joining());
    }

    private String convertEntryToString(Map.Entry<String, DataModel> elem) {
        String key = elem.getKey();
        DataModel value = elem.getValue();
        int size_key = key.length();
        int size_value = value.getData().toString().length();
        return (char) size_key + key + (char) size_value
                + value.getData().toString() + value.getTimeToDelete() + "\n";
    }

    @Override
    public void load(String data) {
        database = Arrays.stream(data.split("\n"))
                .map(this::parseDumpRow)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<String, DataModel> parseDumpRow(String row) {
        int size_key = row.charAt(0);
        String key = row.substring(1, 1 + size_key);
        int size_value = row.charAt(size_key + 1);
        Object value = row.substring(size_key + 2, size_value + size_key + 2);
        long ttd = Long.parseLong(row.substring(size_value + size_key + 2));
        return Map.entry(key, new DataModel(value, ttd));
    }
}

