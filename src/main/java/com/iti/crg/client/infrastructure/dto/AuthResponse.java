package com.iti.crg.client.infrastructure.dto;

import com.iti.crg.client.domain.entities.Player;

import java.util.Set;

public class AuthResponse {
    private boolean success;
    private int score;

    public AuthResponse(boolean success, int score) {
        this.success = success;
        this.score = score;
    }

    public boolean isSuccess() { return success; }
    public int getScore() { return score; }
}

