package com.iti.crg.client.controllers.utils;

public enum View {
    AUTH("auth.fxml"),
    HOME("home.fxml"),
    GAME_BOARD("gameBoard.fxml"),
    GAME_INVITATION("game_invitation.fxml"),
    LOSE_SCREEN("loseScreen.fxml"),
    OFFLINE_VIEW("OfflineView.fxml"),
    ONLINE_LOBBY("onlinePlayers.fxml"), // Assuming this matches OnlineLobbyController
    PLAYER_SETUP("PlayerSetup.fxml"),
    WIN_SCREEN("WinScreen.fxml"),
    DIFFICULTY("difficulty.fxml");
    
    private final String fxmlName;

    View(String fxmlName) {
        this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}