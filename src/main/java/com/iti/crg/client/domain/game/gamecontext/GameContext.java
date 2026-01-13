package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.game.gamehandling.GameHandling;

public abstract class GameContext {
    

    protected GameHandling game;
    protected boolean isRecorded = false;
    
    public GameContext(GameHandling game) {
        this.game = game;
        this.game.startGame();
    }

    public GameHandling getGame() {
        return game;
    }

    public abstract void processMove(int row, int col, GameCallback callback);

    public interface GameCallback {
        void onMoveMade(int row, int col, char symbol);
        void onGameWin(char winner);
        void onGameTie();
        boolean isRecorded();
        void buttonsActive(boolean value);
        void onOpponentDisconnected();
    }
}