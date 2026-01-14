package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.managers.SaveRecordManager;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Handles Local Two Player Mode
 */
public class MultiplePlayerContext extends GameContext {

    private Move[] moves = new Move[9];
    private int moveIndex = 0;

    private String player1Name;
    private String player2Name;

    public MultiplePlayerContext(GameHandling game, String player1Name, String player2Name) {
        super(game);
        this.player1Name = player1Name;
        this.player2Name = player2Name;
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

            if (callback.isRecorded()) {
                saveRecording();
            }

            callback.onGameWin(game.getCurrentPlayer());
            return true;
        }

        if (game.checkTie()) {
            game.endGame();

            if (callback.isRecorded()) {
                saveRecording();
            }

            callback.onGameTie();
            return true;
        }
        return false;
    }

    private void saveRecording() {
        Move[] actualMoves = Arrays.copyOf(moves, moveIndex);

        GameRecord record = new GameRecord(player1Name, player2Name, actualMoves);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String safeP1 = player1Name.replaceAll("\\s+", "");
        String safeP2 = player2Name.replaceAll("\\s+", "");

        String fileName = "Records/Local_" + safeP1 + "_vs_" + safeP2 + "_" + timeStamp + ".record";

        SaveRecordManager.saveInStream(record, fileName);
    }
}