package com.iti.crg.client.controllers;

import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
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
    
    public static String myUsername;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Populate ComboBox here (correct way)
        gameSelector.setItems(FXCollections.observableArrayList("Tic-Tac-Toe"));
        gameSelector.getSelectionModel().selectFirst();

        Set<String> onlineplayers = new HashSet<>();
        new Thread(() -> {
            try {
                var reader = ServerConnection.getInstance().getReader();

                while (true) {
                    int n = Integer.parseInt(reader.readLine());

                    Map<String, Integer> onlinePlayers = new HashMap<>();

                    for (int i = 0; i < n; i++) {
                        String username = reader.readLine();
                        int score = Integer.parseInt(reader.readLine());
                        int status = Integer.parseInt(reader.readLine());

                        if (!myUsername.equals(username)) {
                            onlinePlayers.put(username, score);
                        }
                    }

                    Platform.runLater(() -> {
                        playerList.getChildren().clear();

                        onlinePlayers.forEach((username, score) ->
                            addPlayer(username, score, true)
                        );
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        
        
    }

    private void addPlayer(String name, int score, boolean online) {
        HBox row = new HBox(15);
        row.getStyleClass().add("player-row");

        Circle status = new Circle(6, online ? Color.web("#2ecc71") : Color.web("#bdc3c7"));

        Label username = new Label(name);
        username.getStyleClass().add("player-name");

        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        Button invite = new Button("Invite");
        invite.getStyleClass().add("invite-btn");

        row.getChildren().addAll(status, username, scoreLabel, invite);
        playerList.getChildren().add(row);
    }


    
}