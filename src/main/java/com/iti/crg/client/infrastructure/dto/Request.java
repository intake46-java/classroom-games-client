package com.iti.crg.client.infrastructure.dto;

public class Request {
    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    private String type;
    private String payload;

    public Request(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }
}
