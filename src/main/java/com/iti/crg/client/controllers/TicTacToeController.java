package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import com.iti.crg.client.domain.game.gamecontext.GameContext;
import com.iti.crg.client.domain.game.gamecontext.SinglePlayerContext;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.gamehandling.TicTacToeGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeController implements GameContext.GameCallback {

    // FXML Buttons
    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;

    private Map<String, Button> buttonMap = new HashMap<>();
    private GameContext gameContext;

    @FXML
    public void initialize() {
        // Map grid coordinates (row,col) to buttons for easy access
        // Naming convention from your FXML: b0 is (0,0), b1 is (0,1)... b3 is (1,0)
        buttonMap.put("0,0", b0); buttonMap.put("0,1", b1); buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3); buttonMap.put("1,1", b4); buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6); buttonMap.put("2,1", b7); buttonMap.put("2,2", b8);

        // DEFAULT TO SINGLE PLAYER FOR DEMO (You can change this based on a menu selection)
        startSinglePlayerGame();
    }

    // Call this method from a Main Menu to start Single Player
    public void startSinglePlayerGame() {
        resetButtons();
        GameHandling myGame = new TicTacToeGame();
        gameContext = new SinglePlayerContext(myGame, new MediumTicTacToeAi('O'));
    }

    // Call this method from a Main Menu to start Multi Player
    public void startMultiPlayerGame() {
        resetButtons();

    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Parse coordinate from ID (assuming b0, b1... b8 mapping logic)
        // Or simpler: use GridPane.getRowIndex/ColumnIndex properties
        Integer r = javafx.scene.layout.GridPane.getRowIndex(clickedButton);
        Integer c = javafx.scene.layout.GridPane.getColumnIndex(clickedButton);

        // Handle nulls (GridPane sometimes returns null for index 0)
        int row = (r == null) ? 0 : r;
        int col = (c == null) ? 0 : c;

        // Delegate logic to the Context
        if(gameContext != null) {
            gameContext.processMove(row, col, this);
        }
    }

    // --- Callback Methods from GameContext ---

    @Override
    public void onMoveMade(int row, int col, char symbol) {
        String key = row + "," + col;
        Button btn = buttonMap.get(key);
        if (btn != null) {
            btn.setText(String.valueOf(symbol));
            btn.setDisable(true); // Disable button after move

            // Optional: Styling
            if(symbol == 'X') btn.setStyle("-fx-text-fill: red;");
            else btn.setStyle("-fx-text-fill: blue;");
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

    // --- Helpers ---

    private void resetButtons() {
        for (Button b : buttonMap.values()) {
            b.setText("");
            b.setDisable(false);
            b.setStyle("");
        }
    }

    private void disableAllButtons() {
        for (Button b : buttonMap.values()) {
            b.setDisable(true);
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