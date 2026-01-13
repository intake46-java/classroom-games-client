package com.iti.crg.client.controllers;

import com.google.gson.Gson;
import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OnlineLobbyController implements Initializable {

    @FXML private VBox playerList;
    @FXML private ComboBox<String> gameSelector;
    @FXML private StackPane rootPane;
    @FXML private Label currentUserLabel;
    @FXML private Label currentScoreLabel;

    private volatile boolean listening = true;
    public static String myUsername;
    public static int score;

    private BufferedReader reader;
    private final Gson gson = new Gson();
    private final SendInvitationUseCase sendInvitationUseCase;

    private final Set<String> pendingInvites = new HashSet<>();
    private final Map<String, Button> inviteButtons = new HashMap<>();

    private boolean isDialogActive = false;
    private boolean isWaitingForResponse = false;

    public OnlineLobbyController() {
        this.sendInvitationUseCase = new SendInvitationUseCase();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        AnimatedNetworkBackground background = new AnimatedNetworkBackground(rootPane);
        gameSelector.setItems(FXCollections.observableArrayList("Tic-Tac-Toe"));
        gameSelector.getSelectionModel().selectFirst();

        if (myUsername != null) {
            currentUserLabel.setText(myUsername);
        }
        currentScoreLabel.setText(String.valueOf(score));

        reader = ServerConnection.getInstance().getReader();
        new Thread(this::listenForServerMessages).start();
    }

    private void setAllButtonsDisable(boolean disable) {
        inviteButtons.forEach((username, button) -> {
            if (disable) {
                button.setDisable(true);
            } else {
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

                if (line == null) {
                    System.out.println("Server connection lost (Stream ended).");
                    handleServerDisconnect();
                    break;
                }

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
        } catch (IOException e) {
            if (listening) {
                System.err.println("Connection error: " + e.getMessage());
                handleServerDisconnect();
            }
        }
    }

    private void handleServerDisconnect() {
        listening = false;
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Connection Lost");
            alert.setHeaderText("Server Offline");
            alert.setContentText("The server has shut down or the connection was lost.\nYou will be returned to the main menu.");
            alert.showAndWait();

            logout();
        });
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
                        pendingInvites.remove(rejectUser);
                        isWaitingForResponse = false;
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
        HBox row = new HBox();
        row.getStyleClass().add("player-row");
        row.setAlignment(Pos.CENTER_LEFT);

        HBox infoBox = new HBox(15);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Circle status = new Circle(5, online ? Color.web("#2ecc71") : Color.web("#bdc3c7"));

        VBox nameBox = new VBox(2);
        Label username = new Label(name);
        username.getStyleClass().add("player-name");

        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6;");

        nameBox.getChildren().addAll(username, scoreLabel);
        infoBox.getChildren().addAll(status, nameBox);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button invite = new Button("Invite");
        invite.getStyleClass().add("invite-btn");

        if (pendingInvites.contains(name) || isWaitingForResponse || isDialogActive) {
            invite.setDisable(true);
            if (pendingInvites.contains(name)) invite.setText("Sent...");
        }

        inviteButtons.put(name, invite);

        invite.setOnAction(event -> {
            boolean sent = sendInvitationUseCase.execute(name);
            if (sent) {
                pendingInvites.add(name);
                isWaitingForResponse = true;
                invite.setText("Sent...");
                setAllButtonsDisable(true);
            }
        });

        row.getChildren().addAll(infoBox, spacer, invite);
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
                    GameStartDto startData = gson.fromJson(payload, GameStartDto.class);
                    Navigator.setLast(View.ONLINE_LOBBY);
                    TicTacToeController controller = Navigator.navigate(View.GAME_BOARD);
                    controller.startMultiPlayerGame(myUsername, startData.getOpponent(), startData.getMySymbol(), startData.isTurn());
                }
        );
    }

    @FXML
    private void logout() {
        listening = false;
        ServerConnection.getInstance().forceDisconnect();
        Navigator.navigate(View.HOME);

    }
}