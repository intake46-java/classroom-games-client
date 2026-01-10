package com.iti.crg.client.controllers;

import com.google.gson.Gson;
import com.iti.crg.client.domain.usecases.SendInvitationUseCase;
import com.iti.crg.client.infrastructure.dto.InviteDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.HashSet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OnlineLobbyController implements Initializable {

    @FXML
    private VBox playerList;

    @FXML
    private ComboBox<String> gameSelector;

    public static String myUsername;

    private BufferedReader reader;
    private final Gson gson = new Gson();

    private final SendInvitationUseCase sendInvitationUseCase;

    public OnlineLobbyController() {
        this.sendInvitationUseCase = new SendInvitationUseCase();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        gameSelector.setItems(FXCollections.observableArrayList("Tic-Tac-Toe"));
        gameSelector.getSelectionModel().selectFirst();

        reader = ServerConnection.getInstance().getReader();

        new Thread(this::listenForServerMessages).start();
    }

    private void listenForServerMessages() {
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;

                if (line.trim().startsWith("{")) {
                    handleJsonMessage(line);
                } else {
                    handleRawMessage(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- New Logic (JSON / Use Cases) ---

    private void handleJsonMessage(String json) {
        try {
            Request request = gson.fromJson(json, Request.class);
            String type = request.getType();
            String payload = request.getPayload();

            switch (type) {
                case "INVITE_RECEIVED":
                    // Parse payload to get username
                    InviteDto inviteData = gson.fromJson(payload, InviteDto.class);
                    Platform.runLater(() -> showInviteDialog(inviteData.getUsername()));
                    break;

                case "INVITE_ACCEPTED":
                    InviteDto acceptData = gson.fromJson(payload, InviteDto.class);
                    System.out.println(acceptData.getUsername() + " accepted your invitation");
                    break;

                case "INVITE_REJECTED":
                    InviteDto rejectData = gson.fromJson(payload, InviteDto.class);
                    System.out.println(rejectData.getUsername() + " rejected your invitation");
                    break;

                default:
                    System.out.println("Unknown JSON Request: " + type);
            }
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
    }


    private void handleRawMessage(String type) {
        if ("ONLINE_PLAYERS".equals(type)) {
            readOnlinePlayersLegacy();
        } else {
            System.out.println("Unknown Raw Message: " + type);
        }
    }

    public void readOnlinePlayersLegacy() {
        try {
            String countStr = reader.readLine();
            if (countStr == null) return;

            int n = Integer.parseInt(countStr);
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

    // --- UI Helpers ---

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

        invite.setOnAction(event -> {
            boolean sent = sendInvitationUseCase.execute(name);
            if (sent) {
                System.out.println("Invitation sent to " + name);
                invite.setDisable(true);
            } else {
                System.out.println("Failed to send invitation");
            }
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

            // Safe check for CSS
            URL cssUrl = getClass().getResource("/com/iti/crg/client/game_invitation.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}