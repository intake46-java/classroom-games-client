package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.usecases.RespondToInviteUseCase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game_invitationController implements Initializable {

    @FXML private Label invitationLabel;
    @FXML private Label timerLabel;
    @FXML private Arc timerArc;

    private String fromUser;
    private final RespondToInviteUseCase respondUseCase;

    private Timeline countdownTimeline;
    private static final int TIMEOUT_SECONDS = 20;
    private int timeSeconds = TIMEOUT_SECONDS;

    public Game_invitationController() {
        this.respondUseCase = new RespondToInviteUseCase();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (timerLabel != null) timerLabel.setText(String.valueOf(timeSeconds));
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
        invitationLabel.setText(fromUser + " wants to play!");
        startTimer();
    }

    private void startTimer() {
        countdownTimeline = new Timeline();
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);

        countdownTimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), event -> {
                    timeSeconds--;

                    if (timerLabel != null) {
                        timerLabel.setText(String.valueOf(timeSeconds));
                    }

                    if (timerArc != null) {
                        double progress = (double) timeSeconds / TIMEOUT_SECONDS;
                        timerArc.setLength(progress * 360);
                    }

                    if (timeSeconds <= 0) {
                        stopTimer();
                        onReject(null);
                    }
                })
        );
        countdownTimeline.play();
    }

    private void stopTimer() {
        if (countdownTimeline != null) countdownTimeline.stop();
    }

    @FXML
    private void onAccept(ActionEvent event) {
        stopTimer();
        if (fromUser != null) respondUseCase.accept(fromUser);
        closeStage();
    }

    @FXML
    public void onReject(ActionEvent event) {
        stopTimer();
        if (fromUser != null) respondUseCase.reject(fromUser);
        closeStage();
    }

    private void closeStage() {
        if (invitationLabel.getScene() != null) {
            Stage stage = (Stage) invitationLabel.getScene().getWindow();
            stage.close();
        }
    }
}