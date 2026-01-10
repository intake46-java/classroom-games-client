package com.iti.crg.client.controllers;

import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.PrintStream;

public class Game_invitationController implements Initializable {

    @FXML
    private Label invitationLabel;

    private String fromUser;
    private PrintStream ps;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ps = ServerConnection.getInstance().getWriter();
    }

    // يتم استدعاؤها من OnlinePlayersController بعد load
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
        invitationLabel.setText("Player " + fromUser + " invited you to play Tic Tac Toe"
        );
    }

    @FXML
    private void onAccept(ActionEvent event) {
        ps.println("INVITE-ACCEPT");
        ps.println(fromUser);
        ps.flush();
        closeStage();
    }

    @FXML
    private void onReject(ActionEvent event) {
        ps.println("INVITE-REJECT");
        ps.println(fromUser);
        ps.flush();
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) invitationLabel.getScene().getWindow();
        stage.close();
    }
}
