package com.iti.crg.client.domain.repository;

import com.iti.crg.client.infrastructure.dto.GameMoveDto;

public interface GameRepository {
    boolean sendMove(GameMoveDto moveDto);
    void sendWin(String opponent); // Notify server/opponent of end game
    void sendTie(String opponent);
    void sendLeave(String opponent);
}