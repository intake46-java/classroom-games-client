package com.iti.crg.client.domain.aistrategy;

import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.gamecontext.GameContext;

public interface AiStrategy {
    public Cell AiMove(Board board);
}
