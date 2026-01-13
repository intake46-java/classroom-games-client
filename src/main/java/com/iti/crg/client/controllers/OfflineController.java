package com.iti.crg.client.controllers;

import java.io.IOException;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.View;
import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;

import java.util.ResourceBundle;
import static com.iti.crg.client.controllers.utils.Navigator.navigate;

public class OfflineController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private Button backButton;
    @FXML private VBox cardComputer;
    @FXML private VBox cardLocal;
    private AnimatedNetworkBackground background;

    private static final int PARTICLE_COUNT = 60; // Adjust for density
    private AnimationTimer timer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        background = new AnimatedNetworkBackground(rootPane);
        setupCardHover(cardComputer);
        setupCardHover(cardLocal);
        setupButtonHover(backButton);
    }


    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back to Welcome Screen clicked");
    }

    @FXML
    private void handlePlayComputer(javafx.scene.input.MouseEvent event) {
        System.out.println("Play vs Computer clicked");
        
//        navigate(View.GAME_BOARD);
        navigate(View.DIFFICULTY);
    }

    @FXML
    private void handlePlayLocal(javafx.scene.input.MouseEvent event) {
        System.out.println("Play vs Local Player clicked");
    }


    private void setupCardHover(Node node) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        node.setOnMouseEntered(e -> scaleUp.playFromStart());
        node.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    private void setupButtonHover(Button btn) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), btn);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), btn);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        btn.setOnMouseEntered(e -> scaleUp.playFromStart());
        btn.setOnMouseExited(e -> scaleDown.playFromStart());
    }
}