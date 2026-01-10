package com.iti.crg.client.controllers;

import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OnlinePlayersController implements Initializable{

    @FXML
    private VBox playerList;

    @FXML
    private ComboBox<String> gameSelector;
    
    public static String myUsername;
    BufferedReader reader;
    PrintStream ps;
    private final Object sendLock = new Object();

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Populate ComboBox here (correct way)
        gameSelector.setItems(FXCollections.observableArrayList("Tic-Tac-Toe"));
        gameSelector.getSelectionModel().selectFirst();
        reader = ServerConnection.getInstance().getReader();
        ps = ServerConnection.getInstance().getWriter();

        Set<String> onlineplayers = new HashSet<>();
        new Thread(() -> {
            try {
                
                while (true) {
                    String type = reader.readLine();
                    switch (type) {
                        case "ONLINE_PLAYERS":
                            readOnlinePlayers();
                            break;
                        case "INVITE":
                            String fromUser = reader.readLine();
                            Platform.runLater(() -> showInviteDialog(fromUser));
                            break;

                        case "INVITE-ACCEPTED":
                            System.out.println(reader.readLine() + " accepted your invitation");
                            break;

                        case "INVITE-REJECTED":
                            System.out.println(reader.readLine() + " rejected your invitation");
                            break;

                        default:
                            throw new AssertionError();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        
    }

    public void readOnlinePlayers(){
        try {
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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
        invite.setOnAction(event->{
            new Thread(()->{
                synchronized(sendLock){
                    ps.println("SEND-INVITE");
                    ps.println(name);
                }
            }).start();
            
        });
        
        
        row.getChildren().addAll(status, username, scoreLabel, invite);
        playerList.getChildren().add(row);
    }


    private void showInviteDialog(String fromUser) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/iti/crg/client/Game_invitation.fxml")
            );

            Parent root = loader.load();

            Game_invitationController controller = loader.getController();
            controller.setFromUser(fromUser);

            Stage stage = new Stage();
            stage.setTitle("Game Invitation");
            stage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);

            // Add your CSS file here
            scene.getStylesheets().add(
                getClass().getResource("/com/iti/crg/client/game_invitation.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}