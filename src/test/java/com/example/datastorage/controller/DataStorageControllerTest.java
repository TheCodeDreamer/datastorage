package com.example.datastorage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DataStorageControllerTest {

    private final String PORT = "8080";
    private final String URL = "http://localhost:" + PORT;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getNotExistingObject() {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL).path("/get")
                .queryParam("key", "123").build().toUri();
        ResponseEntity<?> response = this.testRestTemplate.getForEntity(uri, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteNotExistingObject() {
        URI uri = UriComponentsBuilder.fromHttpUrl(URL).path("/delete")
                .queryParam("key", "123").build().toUri();
        ResponseEntity<?> response = this.testRestTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}