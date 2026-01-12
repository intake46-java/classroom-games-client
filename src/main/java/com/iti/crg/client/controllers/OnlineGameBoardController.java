package com.iti.crg.client.controllers;

import com.google.gson.Gson;
import com.iti.crg.client.infrastructure.dto.Request;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class OnlineGameBoardController {

    @FXML private Label playerXLabel;
    @FXML private Label playerOLabel;
    @FXML private Label turnLabel;

    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private Button exitButton;
    @FXML private Button replayButton;

    private Map<String, Button> buttonMap = new HashMap<>();

    private PrintStream ps;
    private BufferedReader reader;
    private String myUsername;
    private String opponent;

    private char mySymbol = 'X';
    private char opponentSymbol = 'O';

    private boolean myTurn = false;
    private boolean gameActive = true;

    private final Gson gson = new Gson();

    // -------------------------
    // Initialization
    // -------------------------

    public void initializeMatch(String opponent, String myUsername, PrintStream ps, BufferedReader reader) {

        this.opponent = opponent;
        this.myUsername = myUsername;
        this.ps = ps;
        this.reader = reader;

        playerXLabel.setText(myUsername + " (X)");
        playerOLabel.setText(opponent + " (O)");

        mapButtons();

        listenToServer();

        turnLabel.setText("Waiting for opponent...");
    }

    private void mapButtons() {
        buttonMap.put("0,0", b0);
        buttonMap.put("0,1", b1);
        buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3);
        buttonMap.put("1,1", b4);
        buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6);
        buttonMap.put("2,1", b7);
        buttonMap.put("2,2", b8);
    }

    // -------------------------
    // LISTEN TO SERVER
    // -------------------------

    private void listenToServer() {
        new Thread(() -> {
            try {
                while (true) {
                    String json = reader.readLine();
                    if (json == null) break;

                    Request req = gson.fromJson(json, Request.class);

                    switch (req.getType()) {

                        case "GAME_START":
                            Platform.runLater(() -> {
                                myTurn = true;
                                turnLabel.setText("Your turn!");
                            });
                            break;

                        case "OPPONENT_MOVE":
                            handleOpponentMove(req.getPayload());
                            break;

                        case "GAME_END":
                            handleGameEnd(req.getPayload());
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server");
            }
        }).start();
    }

    // -------------------------
    // HANDLE OPPONENT MOVE
    // -------------------------

    private void handleOpponentMove(String payload) {
        MoveDto move = gson.fromJson(payload, MoveDto.class);

        Platform.runLater(() -> {
            Button btn = buttonMap.get(move.row + "," + move.col);
            if (btn != null) {
                btn.setText(String.valueOf(opponentSymbol));
                btn.setDisable(true);
            }

            myTurn = true;
            turnLabel.setText("Your turn!");
        });
    }

    // -------------------------
    // HANDLE MY MOVE (UI CLICK)
    // -------------------------

    @FXML
    private void handleMove(javafx.event.ActionEvent event) {
        if (!myTurn || !gameActive) return;

        Button clicked = (Button) event.getSource();

        Integer row = getRow(clicked);
        Integer col = getCol(clicked);

        if (row == null || col == null) return;
        if (!clicked.getText().isEmpty()) return;

        clicked.setText(String.valueOf(mySymbol));
        clicked.setDisable(true);

        sendMove(row, col);

        myTurn = false;
        turnLabel.setText("Opponent's turn...");
    }

    private Integer getRow(Button b) {
        Integer r = javafx.scene.layout.GridPane.getRowIndex(b);
        return (r == null ? 0 : r);
    }

    private Integer getCol(Button b) {
        Integer c = javafx.scene.layout.GridPane.getColumnIndex(b);
        return (c == null ? 0 : c);
    }

    // -------------------------
    // SENDING MOVE TO SERVER
    // -------------------------

    private void sendMove(int row, int col) {
        MoveDto move = new MoveDto(row, col);
        Request req = new Request("SEND_MOVE", gson.toJson(move));

        ps.println(gson.toJson(req));
        ps.flush();
    }

    // -------------------------
    // GAME END
    // -------------------------

    private void handleGameEnd(String payload) {
        GameEndDto end = gson.fromJson(payload, GameEndDto.class);

        Platform.runLater(() -> {
            gameActive = false;

            if (end.winner.equals(myUsername)) {
                turnLabel.setText("You WIN!");
            } else if (end.winner.equals("TIE")) {
                turnLabel.setText("Game Tied!");
            } else {
                turnLabel.setText("You LOST!");
            }

            disableAllButtons();
        });
    }

    private void disableAllButtons() {
        for (Button b : buttonMap.values()) {
            b.setDisable(true);
        }
    }

    // -------------------------
    // EXIT / REPLAY BUTTONS
    // -------------------------

    @FXML
    private void handleExit() {
        gameActive = false;
        disableAllButtons();
        System.out.println("Exit clicked — return to menu here");
    }

    @FXML
    private void handleReplay() {
        System.out.println("Replay request — depends on your design");
    }

    // -------------------------
    // DTO CLASSES
    // -------------------------

    private static class MoveDto {
        int row;
        int col;
        MoveDto(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class GameEndDto {
        String winner;
    }
}