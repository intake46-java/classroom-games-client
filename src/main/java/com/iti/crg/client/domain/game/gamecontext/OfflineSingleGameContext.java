package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.entities.TicTacToeBoard;
import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
import com.iti.crg.client.domain.game.gamehandling.TicTacToeGame;

public class OfflineSingleGameContext extends GameContext{
    
    public AiStrategy aiStrategy;
    public TicTacToeGame ticTacToeGame ;
//    public TicTacToeBoard ticTacToeBoard;
    
    public OfflineSingleGameContext(AiStrategy aiStrategy){
        this.aiStrategy = aiStrategy;
        ticTacToeGame = new TicTacToeGame();
//        ticTacToeBoard = new TicTacToeBoard();
    }
    
    
    
    
    
    
}
