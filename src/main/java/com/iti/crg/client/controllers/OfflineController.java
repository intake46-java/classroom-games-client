package com.iti.crg.client.controllers;

import java.io.IOException;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        
        navigate(event, "gameBoard");
    }

    @FXML
    private void handlePlayLocal(javafx.scene.input.MouseEvent event) {
        System.out.println("Play vs Local Player clicked");
         navigate(event,"gameBoard");
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



    
    //added by mina
    private void navigate(javafx.scene.input.MouseEvent event, String screen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/"+screen+".fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}