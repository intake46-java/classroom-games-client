package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.game.aistrategy.*;
import com.iti.crg.client.domain.game.gamecontext.*;
import com.iti.crg.client.domain.game.gamehandling.GameHandling;
import com.iti.crg.client.domain.game.gamehandling.TicTacToeGame;
import com.iti.crg.client.controllers.utils.ScoreManager;

import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class TicTacToeController implements GameContext.GameCallback {

    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private GridPane boardGrid;
    @FXML private Label turnLabel;
    @FXML private StackPane rootPane;
    @FXML private Label playerNameLabel;
    @FXML private Label opponentNameLabel;
    @FXML private Label opponentScoreLabel;
    @FXML private Label playerScoreLabel;
    @FXML private HBox recordingIndicator; // The new REC badge

    private Map<String, Button> buttonMap = new HashMap<>();
    private GameContext gameContext;
    private boolean isOnlineGame = false;
    private char mySymbol = 'X';
    private boolean isRecorded = false;
    public static TicTacToeController lastController;


    @FXML
    public void initialize() {
        AnimatedNetworkBackground background = new AnimatedNetworkBackground(rootPane);
        buttonMap.put("0,0", b0); buttonMap.put("0,1", b1); buttonMap.put("0,2", b2);
        buttonMap.put("1,0", b3); buttonMap.put("1,1", b4); buttonMap.put("1,2", b5);
        buttonMap.put("2,0", b6); buttonMap.put("2,1", b7); buttonMap.put("2,2", b8);
        lastController = this;

        // Add subtle entry animation for the board
        boardGrid.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), boardGrid);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void initComputerGame(String player1Name, String difficulty, boolean isRecorded) {
        setupGameCommon(player1Name, "Computer (" + difficulty + ")", isRecorded);
        this.isOnlineGame = false;
        this.isRecorded = isRecorded;
        AiStrategy aiStrategy;
        switch (difficulty) {
            case "Hard": aiStrategy = new HardTicTacToeAi('O'); break;
            case "Medium": aiStrategy = new MediumTicTacToeAi('O'); break;
            default: aiStrategy = new EasyTicTacToeAi(); break;
        }

        GameHandling myGame = new TicTacToeGame();
        gameContext = new SinglePlayerContext(myGame, aiStrategy, player1Name);
    }

    // Local Multi Player ---
    public void initLocalTwoPlayerGame(String p1, String p2, boolean isRecorded) {
        setupGameCommon(p1, p2, false); // Local usually not recorded here
        this.isOnlineGame = false;
        this.isRecorded = isRecorded;
        GameHandling game = new TicTacToeGame();
        gameContext = new MultiplePlayerContext(game, p1, p2);
    }

    // Online Multi Player ---
    public void startMultiPlayerGame(String myName, String opponentName, char mySymbol, boolean isMyTurn) {
        // Assume online is recorded if the backend says so, or pass it as param.
        // For now, let's say online matches are recorded:
        setupGameCommon(myName, opponentName, true);
        isRecorded = true;
        this.isOnlineGame = true;
        this.mySymbol = mySymbol;

        GameHandling myGame = new TicTacToeGame();
        OnlinePayingContext mContext = new OnlinePayingContext(myGame, myName, opponentName, mySymbol, isMyTurn);
        mContext.startListening(this);
        this.gameContext = mContext;

        updateOnlineTurnUI(isMyTurn);
    }

    // --- HELPER: Common Setup ---
    private void setupGameCommon(String p1, String p2, boolean isRecorded) {
        resetButtons();
        playerNameLabel.setText(p1);
        opponentNameLabel.setText(p2);
        playerScoreLabel.setText(String.valueOf(ScoreManager.getP1Score()));
        opponentScoreLabel.setText(String.valueOf(ScoreManager.getP2Score()));

        // Handle Recording Indicator
        recordingIndicator.setVisible(isRecorded);
        if(isRecorded) {
            // Blinking animation for REC
            FadeTransition ft = new FadeTransition(Duration.seconds(1), recordingIndicator);
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(FadeTransition.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        }
    }

    @FXML
    private void handleMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Prevent clicking if disabled locally (Online Turn Handling)
        if (clickedButton.isDisabled()) return;

        Integer r = GridPane.getRowIndex(clickedButton);
        Integer c = GridPane.getColumnIndex(clickedButton);
        int row = (r == null) ? 0 : r;
        int col = (c == null) ? 0 : c;

        if (gameContext != null) {
            gameContext.processMove(row, col, this);
        }
    }

    @Override
    public void onMoveMade(int row, int col, char symbol) {
        // Ensure UI updates happen on JavaFX Thread
        Platform.runLater(() -> {
            String key = row + "," + col;
            Button btn = buttonMap.get(key);

            if (btn != null) {
                btn.setText(String.valueOf(symbol));
                btn.setDisable(true); // Disable this specific cell

                // Style based on symbol
                if (symbol == 'X') {
                    btn.getStyleClass().add("cell-x");
                } else {
                    btn.getStyleClass().add("cell-o");
                }

                // Animate the pop
                animateMove(btn);

                // HANDLE TURNS FOR ONLINE
                if (isOnlineGame) {
                    boolean isNowMyTurn = (symbol != mySymbol);
                    updateOnlineTurnUI(isNowMyTurn);
                }
            }
        });
    }

    private void updateOnlineTurnUI(boolean isMyTurn) {
        if (isMyTurn) {
            turnLabel.setText("Your Turn");
            turnLabel.setStyle("-fx-text-fill: #5D9CEC;"); // Blue
            setBoardLocked(false);
        } else {
            turnLabel.setText("Waiting for opponent...");
            turnLabel.setStyle("-fx-text-fill: #999;"); // Grey
            setBoardLocked(true);
        }
    }

    private void setBoardLocked(boolean locked) {
        // Disable interaction with the whole board, but keep it visible
        // We iterate buttons to check if they are already played.
        // If locked = true, disable all free buttons.
        // If locked = false, enable all free buttons (buttons with no text).

        buttonMap.values().forEach(b -> {
            if (b.getText().isEmpty()) {
                b.setDisable(locked);
            }
        });
    }

    private void animateMove(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    @Override
    public void onGameWin(char winner) {
        Platform.runLater(() -> {
            if (winner == mySymbol) ScoreManager.increaseScorePlayer1();
            else ScoreManager.increaseScorePlayer2();

            playerScoreLabel.setText(String.valueOf(ScoreManager.getP1Score()));
            opponentScoreLabel.setText(String.valueOf(ScoreManager.getP2Score()));

            disableAllButtons();

            // Brief delay before navigating so user sees the win
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (winner == mySymbol) Navigator.navigate(View.WIN_SCREEN);
                        else Navigator.navigate(View.LOSE_SCREEN);
                    });
                }
            }, 1000);
        });
    }

    @Override
    public void onGameTie() {
        Platform.runLater(() -> {
            disableAllButtons();
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> Navigator.navigate(View.TIE_SCREEN));
                }
            }, 1000);
        });
    }

    @Override
    public boolean isRecorded() {
        return isRecorded;
    }

    @Override
    public void setRecorded(boolean value) {
        isRecorded = value;
    }

    private void disableAllButtons() {
        buttonMap.values().forEach(b -> b.setDisable(true));
    }

    private void resetButtons() {
        for (Button b : buttonMap.values()) {
            b.setText("");
            b.setDisable(false);
            b.getStyleClass().removeAll("cell-x", "cell-o");
            b.setOpacity(1.0);
        }
    }

    @FXML
    private void onExit(ActionEvent event) {
        ScoreManager.setP1Score(0);
        ScoreManager.setP2Score(0);
        Navigator.navigateBack();
    }
}