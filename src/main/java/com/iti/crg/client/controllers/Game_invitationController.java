package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.usecases.RespondToInviteUseCase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Game_invitationController implements Initializable {

    @FXML
    private Label invitationLabel;

    private String fromUser;

    private final RespondToInviteUseCase respondUseCase;

    public Game_invitationController() {
        this.respondUseCase = new RespondToInviteUseCase();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No need to get Writer here anymore
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
        invitationLabel.setText("Player " + fromUser + " invited you to play Tic Tac Toe");
    }

    @FXML
    private void onAccept(ActionEvent event) {
        if (fromUser != null) {
            respondUseCase.accept(fromUser);
            System.out.println("Accepted invite from " + fromUser);

        }
        closeStage();
    }

    @FXML
    private void onReject(ActionEvent event) {
        if (fromUser != null) {
            respondUseCase.reject(fromUser);
            System.out.println("Rejected invite from " + fromUser);
        }
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) invitationLabel.getScene().getWindow();
        stage.close();
    }
}