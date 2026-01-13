package com.iti.crg.client.domain.game.gamecontext;

import com.google.gson.Gson;
import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.managers.SaveRecordManager;
import com.iti.crg.client.domain.repository.GameRepository;
import com.iti.crg.client.infrastructure.dto.GameMoveDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
import com.iti.crg.client.infrastructure.repository.GameRepositoryImp;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class OnlinePayingContext extends GameContext {

    private final String myName;
    private final String opponentUsername;
    private final char mySymbol;
    private boolean isMyTurn;

    private final GameRepository gameRepository;
    private final BufferedReader reader;
    private final Gson gson;
    private volatile boolean listening = true;
    private GameCallback uiCallback;

    // Recording Fields
    private Move[] moves = new Move[9];
    private int moveIndex = 0;

    public OnlinePayingContext(GameHandling game, String myName, String opponentUsername, char mySymbol, boolean isMyTurn) {
        super(game);
        this.myName = myName;
        this.opponentUsername = opponentUsername;
        this.mySymbol = mySymbol;
        this.isMyTurn = isMyTurn;

        this.gameRepository = new GameRepositoryImp();
        this.reader = ServerConnection.getInstance().getReader();
        this.gson = new Gson();
    }

    public void startListening(GameCallback callback) {
        this.uiCallback = callback;
        new Thread(this::listenToServer).start();
    }

    @Override
    public void processMove(int row, int col, GameCallback callback) {
        if (!isMyTurn) {
            System.out.println("Wait! It's not your turn.");
            return;
        }

        if (game.makeMove(row, col)) {

            // 2. Record the Move
            moves[moveIndex++] = new Move(row, col, mySymbol);

            callback.onMoveMade(row, col, mySymbol);

            System.out.println("Sending move: " + row + "," + col + " to " + opponentUsername);
            GameMoveDto moveDto = new GameMoveDto(opponentUsername, row, col, mySymbol);
            gameRepository.sendMove(moveDto);

            if (checkStatus(callback)) return;

            game.changeTurn();
            isMyTurn = false;
        }
    }

    public void leaveGame() {
        listening = false; // Stop listener loop
        gameRepository.sendLeave(opponentUsername);
    }

    private void listenToServer() {
        try {
            while (listening) {
                String line = reader.readLine();
                if (line == null) {
                    handleServerDisconnect();
                    break;
                }

                if (line.trim().startsWith("{")) {
                    Request request = gson.fromJson(line, Request.class);

                    if ("OPPONENT_MOVE".equals(request.getType())) {
                        GameMoveDto move = gson.fromJson(request.getPayload(), GameMoveDto.class);
                        Platform.runLater(() -> handleOpponentMove(move));
                    }
                    // NEW: Handle opponent leaving
                    else if ("OPPONENT_LEFT".equals(request.getType())) {
                        listening = false;
                        Platform.runLater(() -> {
                            if (uiCallback != null) uiCallback.onOpponentDisconnected();
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerDisconnect() {
        listening = false;
        Platform.runLater(() -> {
            if (uiCallback != null) {
                uiCallback.onOpponentDisconnected();
            }
        });
    }

    private void handleOpponentMove(GameMoveDto move) {
        System.out.println("Processing Opponent Move: " + move.getRow() + "," + move.getCol());

        game.makeMove(move.getRow(), move.getCol());

        moves[moveIndex++] = new Move(move.getRow(), move.getCol(), move.getSymbol());

        if (uiCallback != null) {
            uiCallback.onMoveMade(move.getRow(), move.getCol(), move.getSymbol());

            if (game.checkWin()) {
                game.endGame();
                if (uiCallback.isRecorded()) saveRecording(); // Save
                uiCallback.onGameWin(move.getSymbol());
                listening = false;
            } else if (game.checkTie()) {
                game.endGame();
                if (uiCallback.isRecorded()) saveRecording(); // Save
                uiCallback.onGameTie();
                listening = false;
            } else {
                // 5. Unlock Turn
                game.changeTurn();
                isMyTurn = true;
                System.out.println("It is now your turn!");
            }
        }
    }

    private boolean checkStatus(GameCallback callback) {
        if (game.checkWin()) {
            game.endGame();
            if (callback.isRecorded()) saveRecording(); // Save
            callback.onGameWin(mySymbol);
            gameRepository.sendWin(opponentUsername);
            listening = false;
            return true;
        } else if (game.checkTie()) {
            game.endGame();
            if (callback.isRecorded()) saveRecording();
            callback.onGameTie();
            gameRepository.sendTie(opponentUsername);
            listening = false;
            return true;
        }
        return false;
    }

    private void saveRecording() {
        String p1, p2;
        if (mySymbol == 'X') {
            p1 = myName;
            p2 = opponentUsername;
        } else {
            p1 = opponentUsername;
            p2 = myName;
        }

        Move[] actualMoves = Arrays.copyOf(moves, moveIndex);
        GameRecord record = new GameRecord(p1, p2, actualMoves);

        // 3. Generate filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String safeP1 = p1.replaceAll("\\s+", "");
        String safeP2 = p2.replaceAll("\\s+", "");
        String fileName = "Records/Online_" + safeP1 + "_vs_" + safeP2 + "_" + timeStamp + ".record";

        // 4. Save
        SaveRecordManager.saveInStream(record, fileName);
    }
}