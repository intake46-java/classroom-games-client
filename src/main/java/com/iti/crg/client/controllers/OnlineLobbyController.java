package com.iti.crg.client.controllers;

import com.google.gson.Gson;
import com.iti.crg.client.domain.usecases.SendInvitationUseCase;
import com.iti.crg.client.infrastructure.dto.GameStartDto;
import com.iti.crg.client.infrastructure.dto.InviteDto;
import com.iti.crg.client.infrastructure.dto.Request;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
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

    @FXML private VBox playerList;
    @FXML private ComboBox<String> gameSelector;

    private volatile boolean listening = true;
    public static String myUsername;
    private BufferedReader reader;
    private final Gson gson = new Gson();
    private final SendInvitationUseCase sendInvitationUseCase;

    // --- STATE MANAGEMENT ---
    private final Set<String> pendingInvites = new HashSet<>();
    private final Map<String, Button> inviteButtons = new HashMap<>();

    // 1. Flag for Receiver: Is a popup open?
    private boolean isDialogActive = false;

    // 2. NEW Flag for Sender: Are we waiting for someone to accept our invite?
    private boolean isWaitingForResponse = false;

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

    // --- HELPER: Global Button Toggle ---
    // If 'disable' is true, ALL buttons turn off.
    // If 'disable' is false, buttons turn on UNLESS specific logic prevents it.
    private void setAllButtonsDisable(boolean disable) {
        inviteButtons.forEach((username, button) -> {
            if (disable) {
                button.setDisable(true);
            } else {
                // Determine if we should really enable this button
                // 1. Are we blocked by a popup?
                // 2. Are we waiting for an outgoing invite to this specific user?
                // 3. Are we waiting for ANY outgoing invite?
                if (!pendingInvites.contains(username) && !isDialogActive && !isWaitingForResponse) {
                    button.setDisable(false);
                }
            }
        });
    }

    private void listenForServerMessages() {
        try {
            while (listening) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.trim().startsWith("{")) {
                    Request request = gson.fromJson(line, Request.class);
                    if ("GAME_START".equals(request.getType())) {
                        listening = false;
                        handleGameStart(request.getPayload());
                        break;
                    }
                    handleJsonMessage(line);
                } else {
                    handleRawMessage(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void handleJsonMessage(String json) {
        try {
            Request request = gson.fromJson(json, Request.class);
            String type = request.getType();
            String payload = request.getPayload();

            switch (type) {
                case "INVITE_RECEIVED":
                    InviteDto inviteData = gson.fromJson(payload, InviteDto.class);
                    Platform.runLater(() -> showInviteDialog(inviteData.getUsername()));
                    break;
                case "INVITE_ACCEPTED":
                    System.out.println("Accepted.");
                    break;
                case "INVITE_REJECTED":
                    InviteDto rejectData = gson.fromJson(payload, InviteDto.class);
                    String rejectUser = rejectData.getUsername();
                    Platform.runLater(() -> {
                        // Clear flags
                        pendingInvites.remove(rejectUser);

                        // IMPORTANT: We are no longer waiting for a response
                        isWaitingForResponse = false;

                        // Re-enable everyone (unless dialog is open)
                        setAllButtonsDisable(false);
                    });
                    break;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void handleRawMessage(String type) {
        if ("ONLINE_PLAYERS".equals(type)) {
            readOnlinePlayersLegacy();
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
                inviteButtons.clear();
                onlinePlayers.forEach((username, score) -> addPlayer(username, score, true));
            });
        } catch (IOException ex) { ex.printStackTrace(); }
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

        // --- CHECK STATE ---
        // Disable if:
        // 1. We are waiting for THIS specific user
        // 2. We are waiting for SOMEONE ELSE (Sender block)
        // 3. We are viewing an invite (Receiver block)
        if (pendingInvites.contains(name) || isWaitingForResponse || isDialogActive) {
            invite.setDisable(true);
            if (pendingInvites.contains(name)) invite.setText("Sent...");
        }

        inviteButtons.put(name, invite);

        invite.setOnAction(event -> {
            boolean sent = sendInvitationUseCase.execute(name);
            if (sent) {
                // 1. Mark that we are waiting
                pendingInvites.add(name);
                isWaitingForResponse = true;

                // 2. Set this specific button text
                invite.setText("Sent...");

                // 3. Disable ALL buttons immediately
                setAllButtonsDisable(true);
            }
        });

        row.getChildren().addAll(status, username, scoreLabel, invite);
        playerList.getChildren().add(row);
    }

    private void showInviteDialog(String fromUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/Game_invitation.fxml"));
            Parent root = loader.load();
            Game_invitationController controller = loader.getController();
            controller.setFromUser(fromUser);

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(playerList.getScene().getWindow());
            stage.setScene(new Scene(root));

            isDialogActive = true;
            setAllButtonsDisable(true);

            stage.setOnHidden(e -> {
                isDialogActive = false;
                setAllButtonsDisable(false);
            });

            stage.setOnCloseRequest(e -> controller.onReject(null));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void handleGameStart(String payload) {
        Platform.runLater(() -> {
            try {
                GameStartDto startData = gson.fromJson(payload, GameStartDto.class);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/gameBoard.fxml"));
                Parent root = loader.load();
                TicTacToeController controller = loader.getController();
                controller.startMultiPlayerGame(startData.getOpponent(), startData.getMySymbol(), startData.isTurn());

                Stage stage = (Stage) playerList.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) { e.printStackTrace(); }
        });
    }
}