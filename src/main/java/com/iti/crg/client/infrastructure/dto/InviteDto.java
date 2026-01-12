package com.iti.crg.client.infrastructure.dto;

public class InviteDto {
    private String username;

    public InviteDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}