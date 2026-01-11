package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import com.iti.crg.client.domain.game.gamecontext.GameContext;
import com.iti.crg.client.domain.game.gamecontext.MultiPlayerContext;
import com.iti.crg.client.domain.game.gamecontext.SinglePlayerContext;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.gamehandling.TicTacToeGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeController implements GameContext.GameCallback {

    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private Label turnLabel; // Add a Label in your FXML to show status

    private Map<String, Button> buttonMap = new HashMap<>();
    private GameContext gameContext;

    @FXML
    public void initialize() {
        buttonMap.put("0,0", b0); buttonMap.put("0,1", b1); buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3); buttonMap.put("1,1", b4); buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6); buttonMap.put("2,1", b7); buttonMap.put("2,2", b8);
        startSinglePlayerGame();
    }

    public void startSinglePlayerGame() {
        resetButtons();
        GameHandling myGame = new TicTacToeGame();
        gameContext = new SinglePlayerContext(myGame, new MediumTicTacToeAi('O'), true);
    }

    // Called from OnlineLobbyController
    public void startMultiPlayerGame(String opponentName, char mySymbol, boolean isMyTurn) {
        resetButtons();

        GameHandling myGame = new TicTacToeGame();

        // 1. Create Context
        MultiPlayerContext mContext = new MultiPlayerContext(
                myGame,
                opponentName,
                mySymbol,
                isMyTurn
        );

        // 2. Link Callback & Start Listener
        mContext.startListening(this);
        this.gameContext = mContext;

        // 3. Set Initial UI State
        if (turnLabel != null) {
            turnLabel.setText(isMyTurn ? "Your Turn (" + mySymbol + ")" : "Opponent's Turn");
        }
        System.out.println("Game Started. You are " + mySymbol + ". Turn: " + isMyTurn);
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Integer r = javafx.scene.layout.GridPane.getRowIndex(clickedButton);
        Integer c = javafx.scene.layout.GridPane.getColumnIndex(clickedButton);
        int row = (r == null) ? 0 : r;
        int col = (c == null) ? 0 : c;

        if (gameContext != null) {
            gameContext.processMove(row, col, this);
        }
    }

    @Override
    public void onMoveMade(int row, int col, char symbol) {
        String key = row + "," + col;
        Button btn = buttonMap.get(key);
        if (btn != null) {
            btn.setText(String.valueOf(symbol));
            btn.setDisable(true);
            btn.setStyle(symbol == 'X' ? "-fx-text-fill: red;" : "-fx-text-fill: blue;");
        }

        // Update Turn Label (Optional visual helper)
        if (turnLabel != null) {
            if (gameContext instanceof MultiPlayerContext) {
                // Logic to toggle text, or just generic "Wait" vs "Go"
                // Since we don't expose isMyTurn directly, simpler to just set generic text or leave it
            }
        }
    }

    @Override
    public void onGameWin(char winner) {
        showAlert("Game Over", winner + " Wins!");
        disableAllButtons();
    }

    @Override
    public void onGameTie() {
        showAlert("Game Over", "It's a Tie!");
    }

    private void disableAllButtons() {
        buttonMap.values().forEach(b -> b.setDisable(true));
    }

    private void resetButtons() {
        for (Button b : buttonMap.values()) {
            b.setText("");
            b.setDisable(false);
            b.setStyle("");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}