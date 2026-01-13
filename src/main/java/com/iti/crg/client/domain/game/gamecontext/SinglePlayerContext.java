package com.iti.crg.client.domain.game.gamecontext;

import com.iti.crg.client.domain.entities.Board;
import com.iti.crg.client.domain.entities.Cell;
import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.managers.SaveRecordManager;
import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SinglePlayerContext extends GameContext {

    private AiStrategy aiStrategy;
    public Move[] moves = new Move[9];
    private int moveIndex = 0;

    private String player1Name;
    private String player2Name = "Computer";

    public SinglePlayerContext(GameHandling game, AiStrategy aiStrategy, String player1Name) {
        super(game);
        this.aiStrategy = aiStrategy;
        this.player1Name = player1Name;
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

            new Thread(() -> {
                try {
                    Thread.sleep(700); // AI Thinking delay
                } catch (InterruptedException ignored) {}

                Cell aiMove = aiStrategy.AIMove((Board) game);

                Platform.runLater(() -> {
                    if (aiMove != null) {
                        game.makeMove(aiMove.getRow(), aiMove.getCol());
                        moves[moveIndex++] = new Move(aiMove.getRow(), aiMove.getCol(), game.getCurrentPlayer());
                        callback.onMoveMade(aiMove.getRow(), aiMove.getCol(), game.getCurrentPlayer());

                        if (checkStatus(callback)) {
                            return;
                        }
                        game.changeTurn();
                    }
                });
            }).start();
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
        } else if (game.checkTie()) {
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

        String fileName = "Records/" + safeP1 + "_vs_" + safeP2 + "_" + timeStamp + ".record";

        SaveRecordManager.saveInStream(record, fileName);
    }
}