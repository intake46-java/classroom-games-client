package com.iti.crg.client.infrastructure.dto;

import com.iti.crg.client.domain.entities.Player;

import java.util.Set;

public class AuthResponse {
    private boolean success;
    private Set<Player> onlinePlayers;

    public AuthResponse(boolean success, Set<Player> onlinePlayers) {
        this.success = success;
        this.onlinePlayers = onlinePlayers;
    }

    public boolean isSuccess() { return success; }
    public Set<Player> getOnlinePlayers() { return onlinePlayers; }
}

