package com.iti.crg.client.domain.game.gamehandling;

import com.iti.crg.client.domain.entities.Cell;

public interface GameHandling {
      void startGame();
      void endGame();

      // Returns true if the move was valid and successful
      boolean makeMove(int row, int col);
      void changeTurn();

      // Status Checks
      boolean checkWin();
      boolean checkTie();
      boolean isGameOver();

      // Data for UI
      char getCurrentPlayer(); // Or return a Player object/String for generic usage
}