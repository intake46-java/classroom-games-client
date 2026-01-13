/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.iti.crg.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.iti.crg.client.controllers.utils.View;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import static com.iti.crg.client.controllers.utils.Navigator.navigate;
import static com.iti.crg.client.controllers.utils.Navigator.setLast;

/**
 * FXML Controller class
 *
 * @author dell
 */
   

public class PlayerSetupController {

    @FXML
    private TextField player1Field;

    @FXML
    private TextField player2Field;

    @FXML
    private CheckBox recordGameCheckbox;

    /**
     * This method is called when the "Start Game" button is clicked.
     */
     public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    @FXML
    private void handleStartGame(ActionEvent event) {
        String p1Name = player1Field.getText();
        String p2Name = player2Field.getText();
        boolean isRecorded = recordGameCheckbox.isSelected();

        // Validation: Ensure names are not empty
        if (p1Name.isEmpty() || p2Name.isEmpty()) {
            System.out.println("Please enter names for both players.");
            // You could add a CSS class here to turn the border red if empty
            return;
        }
        setLast(View.PLAYER_SETUP);
        TicTacToeController controller = navigate(View.GAME_BOARD);
        controller.initLocalTwoPlayerGame(p1Name, p2Name, isRecorded);
    
        System.out.println("Starting game...");
        System.out.println("Player 1: " + p1Name);
        System.out.println("Player 2: " + p2Name);
        System.out.println("Recording enabled: " + isRecorded);

    }
    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back to Welcome Screen clicked");
        navigate(View.OFFLINE_VIEW);
    }
}

