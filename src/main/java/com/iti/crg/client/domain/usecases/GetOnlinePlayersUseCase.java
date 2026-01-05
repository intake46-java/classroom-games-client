package com.iti.crg.client.domain.usecases;

import com.iti.crg.client.domain.entities.Player;
import com.iti.crg.client.domain.repository.AuthRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GetOnlinePlayersUseCase {
    private Set<Player> onlinePlayers = Collections.synchronizedSet(new HashSet<>());

    public GetOnlinePlayersUseCase(Set<Player> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public void setOnlinePlayers(Set<Player> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public Set<Player> getOnlinePlayers() {
        return onlinePlayers;
    }
}
