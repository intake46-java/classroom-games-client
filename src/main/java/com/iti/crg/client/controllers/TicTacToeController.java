package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.game.aistrategy.AiStrategy;
import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import com.iti.crg.client.domain.game.gamecontext.GameContext;
import com.iti.crg.client.domain.game.gamecontext.MultiplePlayerContext;
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
import com.iti.crg.client.controllers.utils.ScoreManager;

public class TicTacToeController implements GameContext.GameCallback {

    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private Label turnLabel;
    @FXML private Label playerNameLabel;
    @FXML private Label opponentNameLabel;
    @FXML private Label opponentScoreLabel;
    @FXML private Label playerScoreLabel;

    private Map<String, Button> buttonMap = new HashMap<>();
    private GameContext gameContext;
    private boolean isOnlineGame = false;
    // Store data if needed later
    private String player1Name;
    private String player2Name;
    private boolean isRecorded;
    private char mySymbol = 'X';
    private boolean isLocalTwoPlayer = false;
    private boolean isSinglePlayer = false;
    private String difficulty;
    public static TicTacToeController lastController;
    
    @FXML
    public void initialize() {
        // Just map buttons here. DO NOT start the game yet.
        buttonMap.put("0,0", b0); buttonMap.put("0,1", b1); buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3); buttonMap.put("1,1", b4); buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6); buttonMap.put("2,1", b7); buttonMap.put("2,2", b8);
        lastController = this;
    }

    // --- NEW METHOD: Called from Setup Screen ---
    public void initComputerGame(String player1Name, String difficulty, boolean isRecorded) {
        this.player1Name = player1Name;
        this.player2Name = "Computer (" + difficulty + ")";
        this.isRecorded = isRecorded;
        this.isOnlineGame = false;
        this.isSinglePlayer = true;
        this.isLocalTwoPlayer = false;
        this.difficulty = difficulty;
        resetButtons();

        playerNameLabel.setText(player1Name);
        opponentNameLabel.setText(player2Name);

        playerScoreLabel.setText(String.valueOf(ScoreManager.getP1Score()));
        opponentScoreLabel.setText(String.valueOf(ScoreManager.getP2Score()));

        turnLabel.setText(player1Name + " VS " + player2Name);

        ScoreManager.startNewSession(player1Name, player2Name, true, difficulty);
        
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

        turnLabel.setText(player1Name + " VS " + player2Name);

        GameHandling myGame = new TicTacToeGame();
        gameContext = new SinglePlayerContext(myGame, aiStrategy);

        System.out.println("Starting Single Player: " + player1Name + " | Diff: " + difficulty + " | Rec: " + isRecorded);
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

        if (winner == 'X') ScoreManager.increaseScorePlayer1();
        else ScoreManager.increaseScorePlayer2();

        playerScoreLabel.setText(String.valueOf(ScoreManager.getP1Score()));
        opponentScoreLabel.setText(String.valueOf(ScoreManager.getP2Score()));

        if (isSinglePlayer) {
            if (winner == 'X') Navigator.navigate(View.WIN_SCREEN);
            else Navigator.navigate(View.LOSE_SCREEN);
        } else {
            Navigator.navigate(View.WIN_SCREEN);
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


    @FXML
    private void onExit(ActionEvent event) {
        if(!isOnlineGame) {
            navigate(View.OFFLINE_VIEW);
        }
        ScoreManager.setP1Score(0);
        ScoreManager.setP2Score(0);
    }
    
    public void initLocalTwoPlayerGame(String p1, String p2) {

        this.player1Name = p1;
        this.player2Name = p2;

        this.isSinglePlayer = false;
        this.isLocalTwoPlayer = true;

        resetButtons();

        playerNameLabel.setText(p1);
        opponentNameLabel.setText(p2);

        playerScoreLabel.setText(String.valueOf(ScoreManager.getP1Score()));
        opponentScoreLabel.setText(String.valueOf(ScoreManager.getP2Score()));

        turnLabel.setText(p1 + " VS " + p2);

        ScoreManager.startNewSession(p1, p2, false, null);

        GameHandling game = new TicTacToeGame();
        gameContext = new MultiplePlayerContext(game);
    }
    
}