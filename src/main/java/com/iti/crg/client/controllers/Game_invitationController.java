package com.iti.crg.client.controllers;

import com.google.gson.Gson;
import com.iti.crg.client.infrastructure.dto.InviteDto;
import com.iti.crg.client.infrastructure.dto.Request;
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
    Gson gson = new Gson();
    
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
        InviteDto dto = new InviteDto(fromUser);
        Request req = new Request("INVITE_ACCEPT", gson.toJson(dto));

        ps.println(gson.toJson(req));
        ps.flush();
    }

    @FXML
    private void onReject(ActionEvent event) {
        InviteDto dto = new InviteDto(fromUser);
        Request req = new Request("INVITE_REJECT", gson.toJson(dto));

        ps.println(gson.toJson(req));
        ps.flush();
    }

    private void closeStage() {
        Stage stage = (Stage) invitationLabel.getScene().getWindow();
        stage.close();
    }
}
