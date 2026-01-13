package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;   // Ensure these exist
import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;   // Ensure these exist
import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import com.iti.crg.client.domain.game.gamecontext.GameContext;
import com.iti.crg.client.domain.game.gamecontext.OnlinePayingContext;
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

import static com.iti.crg.client.controllers.utils.Navigator.navigate;

public class TicTacToeController implements GameContext.GameCallback {

    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private Label turnLabel;

    private Map<String, Button> buttonMap = new HashMap<>();
    private GameContext gameContext;
    private boolean isOnlineGame = false;
    // Store data if needed later
    private String playerName;
    private boolean isRecorded;
    private char mySymbol = 'X';

    @FXML
    public void initialize() {
        // Just map buttons here. DO NOT start the game yet.
        buttonMap.put("0,0", b0); buttonMap.put("0,1", b1); buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3); buttonMap.put("1,1", b4); buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6); buttonMap.put("2,1", b7); buttonMap.put("2,2", b8);
    }

    // --- NEW METHOD: Called from Setup Screen ---
    public void initComputerGame(String playerName, String difficulty, boolean isRecorded) {
        this.playerName = playerName;
        this.isRecorded = isRecorded;
        this.isOnlineGame = false;

        resetButtons();

        AiStrategy aiStrategy;
        switch (difficulty) {
            case "Easy":
                aiStrategy = new EasyTicTacToeAi();
                break;
            case "Hard":
                aiStrategy = new HardTicTacToeAi('O');
                break;
            case "Medium":
            default:
                aiStrategy = new MediumTicTacToeAi('O');
                break;
        }

        if (turnLabel != null) {
            turnLabel.setText("Player: " + playerName + " vs Computer (" + difficulty + ")");
        }

        GameHandling myGame = new TicTacToeGame();
        gameContext = new SinglePlayerContext(myGame, aiStrategy);

        System.out.println("Starting Single Player: " + playerName + " | Diff: " + difficulty + " | Rec: " + isRecorded);
    }


    public void startMultiPlayerGame(String myName, String opponentName, char mySymbol, boolean isMyTurn) {
        resetButtons();
        this.mySymbol=mySymbol;
        isOnlineGame = true;
        GameHandling myGame = new TicTacToeGame();
        OnlinePayingContext mContext = new OnlinePayingContext(myGame, opponentName, mySymbol, isMyTurn);
        mContext.startListening(this);
        this.gameContext = mContext;
        if (turnLabel != null) {
            turnLabel.setText(myName + " VS "+opponentName);
        }
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
    }

    @Override
    public void onGameWin(char winner) {
        //showAlert("Game Over", winner + " Wins!");
        isOnlineGame = false;
        if(winner == mySymbol) {
            Navigator.navigate(View.WIN_SCREEN);
        }else {
            Navigator.navigate(View.LOSE_SCREEN);
        }
        disableAllButtons();
    }

    @Override
    public void onGameTie() {
        Navigator.navigate(View.TIE_SCREEN);
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

    @FXML
    private void onExit(ActionEvent event) {
        if(!isOnlineGame) {
            navigate(View.OFFLINE_VIEW); // Or wherever you go back to
        }
    }
}