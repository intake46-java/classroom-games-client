package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ComputerSetupController implements Initializable {

    @FXML private TextField playerNameField;
    @FXML private ComboBox<String> difficultyBox;
    @FXML private CheckBox recordGameCheckbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        difficultyBox.getSelectionModel().select("Medium");
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()) playerName = "Player";

        String difficulty = difficultyBox.getValue();
        boolean isRecorded = recordGameCheckbox.isSelected();

        TicTacToeController gameController = Navigator.navigate(View.GAME_BOARD);

        if (gameController != null) {
            gameController.initComputerGame(playerName, difficulty, isRecorded);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Navigator.navigate(View.OFFLINE_VIEW);
    }
}