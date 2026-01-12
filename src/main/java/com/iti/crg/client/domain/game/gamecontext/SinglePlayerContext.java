package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.entities.Board;
import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.entities.Move;
import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.managers.SaveRecordManager;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinglePlayerContext extends GameContext {

    private AiStrategy aiStrategy;
    public Move[] moves = new Move[9];
    private int moveIndex = 0;

    // Constructor accepts ANY game and ANY strategy
    public SinglePlayerContext(GameHandling game, AiStrategy aiStrategy) {
        super(game);
        this.aiStrategy = aiStrategy;
    }

    @Override
    public void processMove(int row, int col, GameCallback callback) {
        if (game.isGameOver()) {
            return;
        }
        // 1. Human Move
        if (game.makeMove(row, col)) {
            moves[moveIndex++] = new Move(row, col, game.getCurrentPlayer());
            callback.onMoveMade(row, col, game.getCurrentPlayer());

            if (checkStatus(callback)) {
                return;
            }

            game.changeTurn();

            // 2. AI Move (Note: AI needs to cast or use the generic board)
            // Ideally, AiStrategy should accept 'GameHandling' or 'Board'
            // For now, assuming your AI takes the board:
            Cell aiMove = aiStrategy.AIMove((Board) game); // Assuming Game extends Board

            if (aiMove != null) {
                game.makeMove(aiMove.getRow(), aiMove.getCol());
                moves[moveIndex++] = new Move(aiMove.getRow(), aiMove.getCol(), game.getCurrentPlayer());
                callback.onMoveMade(aiMove.getRow(), aiMove.getCol(), game.getCurrentPlayer());

                if (checkStatus(callback)) {
                    return;
                }

                game.changeTurn();
            }
        }
    }

    private boolean checkStatus(GameCallback callback) {
        if (game.checkWin()) {
            game.endGame();
            callback.onGameWin(game.getCurrentPlayer());
            return true;
        } else if (game.checkTie()) {
            game.endGame();
            callback.onGameTie();

            return true;
        }
        return false;
    }


}
