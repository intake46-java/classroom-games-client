package com.iti.crg.client.controllers;

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

public class OfflineController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private Canvas backgroundCanvas;
    @FXML private Button backButton;
    @FXML private VBox cardComputer;
    @FXML private VBox cardLocal;

    private final List<Particle> particles = new ArrayList<>();
    private static final int PARTICLE_COUNT = 60; // Adjust for density
    private AnimationTimer timer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupCardHover(cardComputer);
        setupCardHover(cardLocal);
        setupButtonHover(backButton);

        backgroundCanvas.widthProperty().bind(rootPane.widthProperty());
        backgroundCanvas.heightProperty().bind(rootPane.heightProperty());

        initParticles();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawNetworkBackground();
            }
        };
        timer.start();

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> initParticles());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> initParticles());
    }


    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back to Welcome Screen clicked");
    }

    @FXML
    private void handlePlayComputer(javafx.scene.input.MouseEvent event) {
        System.out.println("Play vs Computer clicked");
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

    private void initParticles() {
        particles.clear();
        Random rand = new Random();
        double w = rootPane.getWidth();
        double h = rootPane.getHeight();
        // Ensure w/h are not zero on first run
        if (w == 0) w = 900;
        if (h == 0) h = 600;

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * w, rand.nextDouble() * h));
        }
    }

    private void drawNetworkBackground() {
        GraphicsContext gc = backgroundCanvas.getGraphicsContext2D();
        double w = backgroundCanvas.getWidth();
        double h = backgroundCanvas.getHeight();

        gc.clearRect(0, 0, w, h);

        // Styling for dots and lines
        gc.setLineWidth(0.5);
        gc.setStroke(Color.rgb(180, 180, 200, 0.3)); // Faint bluish-grey lines
        gc.setFill(Color.rgb(160, 160, 180, 0.4));   // Faint dots

        for (Particle p : particles) {
            p.move(w, h);

            // Draw the dot
            gc.fillOval(p.x, p.y, 3, 3);

            // Draw connections to nearby particles
            for (Particle other : particles) {
                double distance = Math.hypot(p.x - other.x, p.y - other.y);
                // Connection threshold distance
                if (distance < 110) {
                    // Opacity fades as distance increases
                    gc.setGlobalAlpha(1.0 - (distance / 110));
                    gc.strokeLine(p.x, p.y, other.x, other.y);
                }
            }
        }
        gc.setGlobalAlpha(1.0); // Reset alpha
    }

    // Inner class for particle data
    private static class Particle {
        double x, y, vx, vy;

        Particle(double x, double y) {
            this.x = x;
            this.y = y;
            // Slow, random velocity
            this.vx = (Math.random() - 0.5) * 0.6;
            this.vy = (Math.random() - 0.5) * 0.6;
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