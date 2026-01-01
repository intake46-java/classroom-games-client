package com.iti.crg.client.controllers;


import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HomeController implements Initializable {

    @FXML private Canvas backgroundCanvas;
    @FXML private VBox contentBox;
    @FXML private Button btnOffline;
    @FXML private Button btnOnline;
    @FXML private Label nameLabel;

    Socket mySocket;

    BufferedReader dis;
    PrintStream ps;

    private final List<Particle> particles = new ArrayList<>();
    private static final int PARTICLE_COUNT = 40;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Initial Entry Animations
        playEntryAnimations();

        // 2. Button Hover Effects
        addPulseEffect(btnOffline);
        addPulseEffect(btnOnline);

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

    private void playEntryAnimations() {
        // Slide Up
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), contentBox);
        tt.setFromY(50);
        tt.setToY(0);

        // Fade In
        FadeTransition ft = new FadeTransition(Duration.millis(1000), contentBox);
        ft.setFromValue(0);
        ft.setToValue(1);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.play();
    }

    private void addPulseEffect(Button btn) {
        ScaleTransition stGrow = new ScaleTransition(Duration.millis(200), btn);
        stGrow.setToX(1.05);
        stGrow.setToY(1.05);

        ScaleTransition stShrink = new ScaleTransition(Duration.millis(200), btn);
        stShrink.setToX(1.0);
        stShrink.setToY(1.0);

        btn.setOnMouseEntered(e -> {
            stGrow.playFromStart();
            btn.setEffect(new Glow(0.5)); // Makes colors pop
        });

        btn.setOnMouseExited(e -> {
            stShrink.playFromStart();
            btn.setEffect(null);
        });
    }

    // --- Background Logic ---

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

    // Simple inner class for particle data
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

    public void setData(String name, Socket mySocket, BufferedReader dis, PrintStream ps ) {
        nameLabel.setText(name);
        this.mySocket = mySocket;
        this.dis = dis;
        this.ps = ps;
    }
    
    
}
