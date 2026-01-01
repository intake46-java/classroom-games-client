package com.iti.crg.client.domain.game.aistrategy;

import com.iti.crg.client.domain.entities.Board;
import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.game.gamecontext.GameContext;

public interface AiStrategy {
    Cell AIMove(Board board);
}
