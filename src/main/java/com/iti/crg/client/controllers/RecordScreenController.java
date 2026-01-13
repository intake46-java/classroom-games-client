package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class RecordScreenController implements Initializable {

    @FXML private GridPane boardGrid;
    @FXML private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML private Label playerXLabel;
    @FXML private Label playerOLabel;
    @FXML private Label statusLabel;

    private Button[] cells;
    private GameRecord record;
    private int currentIndex = -1;
    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cells = new Button[]{b0, b1, b2, b3, b4, b5, b6, b7, b8};
    }

    /** Inject the record from outside (after loading) */
    public void setRecord(GameRecord record) {
        this.record = record;
        clearBoard();
        playerXLabel.setText("Player X: " + record.getPlayer1());
        playerOLabel.setText("Player O: " + record.getPlayer2());
        statusLabel.setText("Ready to replay...");
    }

    private void clearBoard() {
        for (Button cell : cells) {
            cell.setText("");
            cell.getStyleClass().removeAll("cell-x", "cell-o", "cell-win");
        }
        currentIndex = -1;
    }

    private void applyMove(Move move) {
        Button cell = cells[move.getPositionIndex()];
        String symbol = String.valueOf(move.getplayedCharacter());
        cell.setText(symbol);

        if ("X".equals(symbol)) {
            cell.getStyleClass().add("cell-x");
        } else if ("O".equals(symbol)) {
            cell.getStyleClass().add("cell-o");
        }

        checkWinner();
        statusLabel.setText("Move " + (currentIndex + 1) + " of " + record.getMoves().length);
    }

    private void highlightWinner(int[][] win) {
        for (int[] pos : win) {
            int row = pos[0];
            int col = pos[1];
            Button cell = cells[row * 3 + col];
            cell.getStyleClass().add("cell-win");
        }
        statusLabel.setText("Winner detected!");
    }

    private void checkWinner() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = cells[i].getText();
        }

        int[][][] wins = {
            {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}}, // rows
            {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}}, // cols
            {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}}  // diagonals
        };

        for (int[][] win : wins) {
            String a = board[win[0][0]][win[0][1]];
            String b = board[win[1][0]][win[1][1]];
            String c = board[win[2][0]][win[2][1]];
            if (a != null && !a.isEmpty() && a.equals(b) && b.equals(c)) {
                highlightWinner(win);
                break;
            }
        }
    }

    @FXML
    private void handleBack() {
        System.out.println("Back pressed");
    }

    @FXML
    private void handlePrev() {
        if (record == null || currentIndex < 0) return;

        Move move = record.getMoves()[currentIndex];
        Button cell = cells[move.getPositionIndex()];
        cell.setText("");
        cell.getStyleClass().removeAll("cell-x", "cell-o");

        currentIndex = Math.max(-1, currentIndex - 1);
        checkWinner();
        statusLabel.setText("Move " + (currentIndex + 1) + " of " + record.getMoves().length);
         for (Button highLightedcell : cells) {
            highLightedcell.getStyleClass().removeAll( "cell-win");
        }
    }

    @FXML
    private void handleNext() {
        if (record == null || currentIndex >= record.getMoves().length - 1) return;

        currentIndex++;
        applyMove(record.getMoves()[currentIndex]);
    }

    @FXML
    private void handlePlay() {
        if (record == null) return;
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            return; // already playing
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (currentIndex < record.getMoves().length - 1) {
                handleNext();
            } else {
                timeline.stop();
                statusLabel.setText("Replay finished.");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        statusLabel.setText("Playing...");
    }

    @FXML
    private void handlePause() {
        if (timeline != null) {
            timeline.stop();
            statusLabel.setText("Paused at move " + (currentIndex + 1));
        }
    }
}