package com.iti.crg.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OnlinePlayersController implements Initializable{

    @FXML
    private VBox playerList;

    @FXML
    private ComboBox<String> gameSelector;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Populate ComboBox here (correct way)
        gameSelector.setItems(FXCollections.observableArrayList("Tic-Tac-Toe"));
        gameSelector.getSelectionModel().selectFirst();

        addPlayer("Alex_Gamer", true);
        addPlayer("SarahP", false);
        addPlayer("Dr_Chess", true);
        addPlayer("ProPlayer99", true);
        addPlayer("GamerGirl23", true);
        addPlayer("TacticalTom", false);
    }

    private void addPlayer(String name, boolean online) {
        HBox row = new HBox(15);
        row.getStyleClass().add("player-row");

        Circle status = new Circle(6, online ? Color.web("#2ecc71") : Color.web("#bdc3c7"));

        Label username = new Label(name);
        username.getStyleClass().add("player-name");

        Button invite = new Button("Invite");
        invite.getStyleClass().add("invite-btn");

        row.getChildren().addAll(status, username, invite);
        playerList.getChildren().add(row);
    }

    
}