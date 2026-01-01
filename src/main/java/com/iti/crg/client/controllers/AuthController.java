package com.iti.crg.client.controllers;

import com.iti.crg.client.domain.usecases.LoginResult;
import com.iti.crg.client.domain.usecases.LoginUseCase;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    @FXML private Canvas backgroundCanvas;
    private final List<Particle> particles = new ArrayList<>();
    private static final int PARTICLE_COUNT = 40;
    // --- FXML Injections (Make sure these match fx:id in FXML) ---
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button eyeButton;
    @FXML private StackPane passwordContainer;

    // --- Dependencies ---
    private final LoginUseCase loginUseCase = new LoginUseCase();
    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the visibility state
        passwordTextField.setVisible(false);

        // Sync the text between the hidden PasswordField and visible TextField
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        // 3. Start Background Animation
        initParticles();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawNetworkBackground();
            }
        };
        timer.start();
    }

    private void initParticles() {
        Random rand = new Random();
        double w = backgroundCanvas.getWidth(); // Default 900
        double h = backgroundCanvas.getHeight(); // Default 600

        // Create particles randomly distributed
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * w, rand.nextDouble() * h));
        }
    }

    private void drawNetworkBackground() {
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        double w = backgroundCanvas.getWidth();
        double h = backgroundCanvas.getHeight();

        gc.clearRect(0, 0, w, h);

        // Style for the "constellation" lines
        gc.setLineWidth(0.5);
        gc.setStroke(Color.rgb(180, 180, 180, 0.4)); // Light grey lines
        gc.setFill(Color.rgb(160, 160, 160, 0.5));   // Light grey dots

        for (Particle p : particles) {
            p.move(w, h);

            // Draw the dot
            gc.fillOval(p.x, p.y, 3, 3);

            // Draw connections
            for (Particle other : particles) {
                double distance = Math.hypot(p.x - other.x, p.y - other.y);

                // Only connect if close enough
                if (distance < 120) {
                    // Opacity depends on distance (fades out as they get further)
                    gc.setGlobalAlpha(1.0 - (distance / 120));
                    gc.strokeLine(p.x, p.y, other.x, other.y);
                }
            }
        }
        gc.setGlobalAlpha(1.0); // Reset alpha
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
        }
    }

    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        // Disable button to prevent double clicks
        loginButton.setDisable(true);

        // Run Login Logic in Background Thread
        new Thread(() -> {
            LoginResult result = loginUseCase.execute(username, password);

            Platform.runLater(() -> {
                loginButton.setDisable(false);
                if (result.isSuccess()) {
                    navigateToHome(event, username, result);
                } else {
                    showAlert("Login Failed", "Wrong username or password, or server is down.");
                }
            });
        }).start();
    }

    private void navigateToHome(ActionEvent event, String username, LoginResult result) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.getScene().getStylesheets().add(getClass().getResource("/com/iti/crg/client/home.css").toExternalForm());
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("System Error", "Could not load Home Screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class Particle {
        double x, y;
        double vx, vy;

        Particle(double x, double y) {
            this.x = x;
            this.y = y;
            // Very slow, floating movement
            this.vx = (Math.random() - 0.5) * 0.7;
            this.vy = (Math.random() - 0.5) * 0.7;
        }

        void move(double width, double height) {
            x += vx;
            y += vy;
            // Bounce off edges
            if (x < 0 || x > width) vx *= -1;
            if (y < 0 || y > height) vy *= -1;
        }
    }
}