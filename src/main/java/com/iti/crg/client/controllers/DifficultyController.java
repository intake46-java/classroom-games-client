package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DifficultyController implements Initializable {

    @FXML private Button easyBtn;
    @FXML private Button mediumBtn;
    @FXML private Button hardBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void chooseEasy(ActionEvent event) {
        loadGame(event, "EASY");
    }

    @FXML
    private void chooseMedium(ActionEvent event) {
        loadGame(event, "MEDIUM");
    }

    @FXML
    private void chooseHard(ActionEvent event) {
        loadGame(event, "HARD");
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/iti/crg/client/OfflineView.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGame(ActionEvent event, String difficulty) {
        try {
            switch (difficulty) {
                case "EASY":
                    TicTacToeController.setAiStrategy(new EasyTicTacToeAi());
                    break;
                case "MEDIUM":
                    TicTacToeController.setAiStrategy(new MediumTicTacToeAi('O'));
                    break;
                case "HARD":
                    TicTacToeController.setAiStrategy(new HardTicTacToeAi('O'));
                    break;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/gameBoard.fxml"));
            Parent root = loader.load();

            TicTacToeController controller = loader.getController();
            
            // 4. Display GameBoard.fxml
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}