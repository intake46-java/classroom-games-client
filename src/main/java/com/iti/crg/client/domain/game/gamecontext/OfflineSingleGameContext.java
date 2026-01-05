package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.game.aistrategy.AiStrategy;

public class OfflineSingleGameContext extends GameContext{
    
    AiStrategy aiStrategy;
    
    public OfflineSingleGameContext(AiStrategy aiStrategy){
        this.aiStrategy = aiStrategy;
    }
    
    
}
