package com.iti.crg.client.domain.game.gamehandling;

import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.entities.TicTacToeBoard;

public interface GameHandling {
    
      abstract public boolean isValidMove(Cell cell);
      abstract public boolean isWinner(TicTacToeBoard board);
      abstract public boolean isTie(TicTacToeBoard board);
}
