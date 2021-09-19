package com.example.datastorage.service;

import com.example.datastorage.model.DataModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
class DataStorageServiceTest {

    @Autowired
    private DataStorageService dataStorageService;

    private final String KEY = " key";
    private final Object VALUE = "data";

    @Test
    void set() {
        dataStorageService.set(KEY, VALUE, 10000);
        assertEquals(VALUE, dataStorageService.get(KEY).getData().toString());
    }

    @Test
    void checkDeadData() throws InterruptedException {
        dataStorageService.set(KEY, VALUE, 1000);
        Thread.sleep(3000);
        assertNull(dataStorageService.get(KEY));
    }

    @Test
    void remove() {
        dataStorageService.set(KEY, VALUE, 500000);
        DataModel model = dataStorageService.remove(KEY);
        assertEquals(VALUE, model.getData());
    }

    @Test
    void dumpAndLoad() {
        dataStorageService.set(KEY, VALUE, 50000);
        String actual = dataStorageService.dump();
        dataStorageService.load(actual);
        assertEquals(VALUE, dataStorageService.get(KEY).getData());

    }
}