package com.iti.crg.client.domain.game.gamecontext;

import com.google.gson.Gson;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.repository.GameRepository;
import com.iti.crg.client.infrastructure.dto.GameMoveDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
import com.iti.crg.client.infrastructure.repository.GameRepositoryImp;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;

public class MultiPlayerContext extends GameContext {

    private final String opponentUsername;
    private final char mySymbol;
    private boolean isMyTurn;

    private final GameRepository gameRepository;
    private final BufferedReader reader;
    private final Gson gson;
    private volatile boolean listening = true;
    private GameCallback uiCallback; // Needed to update UI

    public MultiPlayerContext(GameHandling game, String opponent, char mySymbol, boolean isMyTurn) {
        super(game);
        this.opponentUsername = opponent;
        this.mySymbol = mySymbol;
        this.isMyTurn = isMyTurn;

        this.gameRepository = new GameRepositoryImp();
        this.reader = ServerConnection.getInstance().getReader();
        this.gson = new Gson();
    }

    // Call this immediately after creating the Context in the Controller
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

        // 1. Update Local Game Logic
        if (game.makeMove(row, col)) {
            // 2. Update UI
            callback.onMoveMade(row, col, mySymbol);

            // 3. Send to Server
            System.out.println("Sending move: " + row + "," + col + " to " + opponentUsername);
            GameMoveDto moveDto = new GameMoveDto(opponentUsername, row, col, mySymbol);
            gameRepository.sendMove(moveDto);

            // 4. Check End Game
            if (checkStatus(callback)) return;

            // 5. Lock Turn
            game.changeTurn();
            isMyTurn = false;
        }
    }

    private void listenToServer() {
        System.out.println("MultiPlayer Listener Started...");
        try {
            while (listening) {
                String line = reader.readLine();
                if (line == null) break;

                System.out.println("Received from Server: " + line); // DEBUG

                if (line.trim().startsWith("{")) {
                    Request request = gson.fromJson(line, Request.class);

                    if ("OPPONENT_MOVE".equals(request.getType())) {
                        GameMoveDto move = gson.fromJson(request.getPayload(), GameMoveDto.class);
                        // Important: Run UI updates on JavaFX Thread
                        Platform.runLater(() -> handleOpponentMove(move));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOpponentMove(GameMoveDto move) {
        System.out.println("Processing Opponent Move: " + move.getRow() + "," + move.getCol());

        // 1. Update Internal Logic
        // Note: We force the move because the server validated it (or we trust the flow)
        game.makeMove(move.getRow(), move.getCol());

        // 2. Update UI
        if (uiCallback != null) {
            uiCallback.onMoveMade(move.getRow(), move.getCol(), move.getSymbol());

            // 3. Check End Game
            if (game.checkWin()) {
                game.endGame();
                uiCallback.onGameWin(move.getSymbol());
                listening = false;
            } else if (game.checkTie()) {
                game.endGame();
                uiCallback.onGameTie();
                listening = false;
            } else {
                // 4. Unlock Turn
                game.changeTurn();
                isMyTurn = true;
                System.out.println("It is now your turn!");
            }
        }
    }

    private boolean checkStatus(GameCallback callback) {
        if (game.checkWin()) {
            game.endGame();
            callback.onGameWin(mySymbol);
            gameRepository.sendWin(opponentUsername); // Optional: Notify server
            listening = false;
            return true;
        } else if (game.checkTie()) {
            game.endGame();
            callback.onGameTie();
            gameRepository.sendTie(opponentUsername); // Optional
            listening = false;
            return true;
        }
        return false;
    }
}