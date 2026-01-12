/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.entities.Move;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;

/**
 *
 * @author dell
 */
public class MultiplePlayerContext extends GameContext {

    private Move[] moves = new Move[9];
    private int moveIndex = 0;

    public MultiplePlayerContext(GameHandling game) {
        super(game);
    }

    @Override
    public void processMove(int row, int col, GameCallback callback) {
        if (game.isGameOver()) {
            return;
        }

        if (game.makeMove(row, col)) {
            moves[moveIndex++] = new Move(row, col, game.getCurrentPlayer());
            callback.onMoveMade(row, col, game.getCurrentPlayer());

            if (checkStatus(callback)) {
                return;
            }
            game.changeTurn();
        }
    }

    private boolean checkStatus(GameCallback callback) {
        if (game.checkWin()) {
            game.endGame();
            callback.onGameWin(game.getCurrentPlayer());
            return true;
        }

        if (game.checkTie()) {

            game.endGame();
            callback.onGameTie();
            return true;
        }
        return false;

    }
}
