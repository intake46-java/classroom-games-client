package com.iti.crg.client.infrastructure.dto;

public class Request {
    private String type;
    private String payload;

    public Request(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }
}
