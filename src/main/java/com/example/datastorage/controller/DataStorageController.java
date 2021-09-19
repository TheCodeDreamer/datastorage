package com.example.datastorage.controller;

import com.example.datastorage.model.DataModel;
import com.example.datastorage.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class DataStorageController {

    private final DataStorageService dataStorageService;

    @Autowired
    public DataStorageController(DataStorageService dataStorageService) {
        this.dataStorageService = dataStorageService;
    }

    @GetMapping("/list")
    public Map<String, DataModel> getAll() {
        return dataStorageService.getAll();
    }

    @GetMapping("/get")
    public ResponseEntity<DataModel> get(@RequestParam String key) {
        DataModel data = dataStorageService.get(key);
        return data != null ? new ResponseEntity<>(data, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/set")
    public ResponseEntity<?> addData(@RequestParam String key, @RequestParam Object data,
                                     @RequestParam(defaultValue = "1_000_000") long ttl) {
        try {
            dataStorageService.set(key, data, ttl);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<DataModel> delete(@RequestParam String key) {
        DataModel data = dataStorageService.remove(key);
        return data != null ? new ResponseEntity<>(data, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/dump")
    public ResponseEntity<?> dumpFile(@RequestParam("filename") String filename) {
        byte[] dump = dataStorageService.dump().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(dump.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dump);
    }

    @PostMapping("/load")
    public ResponseEntity<?> load(@RequestPart("file") MultipartFile file) {
        try {
            dataStorageService.load(new String(file.getInputStream().readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
