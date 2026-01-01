package com.iti.crg.client.domain.aistrategy;

import com.iti.crg.client.domain.gamecontext.GameContext;

public interface AiStrategy {
    public void makeMove();
    public void easyMove();
    public void hardMove();
    public void mediumMove();

}
