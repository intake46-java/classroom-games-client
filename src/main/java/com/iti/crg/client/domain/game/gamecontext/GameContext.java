package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.game.gamehandling.GameHandling;

public abstract class GameContext {
    
    // Changed from 'TicTacToeGame' to the interface 'GameHandling'
    protected GameHandling game;
    
    // Dependency Injection: Pass the specific game implementation here
    public GameContext(GameHandling game) {
        this.game = game;
        this.game.startGame();
    }

    public GameHandling getGame() {
        return game;
    }

    // Abstract method remains the same
    public abstract void processMove(int row, int col, GameCallback callback);

    // Interface to communicate back to the UI Controller
    public interface GameCallback {
        void onMoveMade(int row, int col, char symbol);
        void onGameWin(char winner);
        void onGameTie();
    }
}