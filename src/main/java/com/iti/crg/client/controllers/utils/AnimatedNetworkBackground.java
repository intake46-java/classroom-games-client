package com.iti.crg.client.controllers.utils;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimatedNetworkBackground {

    private final Canvas canvas = new Canvas();
    private final List<Particle> particles = new ArrayList<>();
    private static final int PARTICLE_COUNT = 60;

    private AnimationTimer timer;
    private StackPane root;

    public AnimatedNetworkBackground(StackPane root) {
        this.root = root;

        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        root.getChildren().add(0, canvas);
        initParticles();
        start();
    }

    private void start() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        timer.start();

        root.widthProperty().addListener((o, oldV, newV) -> initParticles());
        root.heightProperty().addListener((o, oldV, newV) -> initParticles());
    }

    private void initParticles() {
        particles.clear();
        Random rand = new Random();

        double w = root.getWidth() == 0 ? 900 : root.getWidth();
        double h = root.getHeight() == 0 ? 600 : root.getHeight();

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(rand.nextDouble() * w, rand.nextDouble() * h));
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.clearRect(0, 0, w, h);

        gc.setLineWidth(0.5);
        gc.setStroke(Color.rgb(180, 180, 200, 0.3));
        gc.setFill(Color.rgb(160, 160, 180, 0.4));

        for (Particle p : particles) {
            p.move(w, h);
            gc.fillOval(p.x, p.y, 3, 3);

            for (Particle other : particles) {
                double dist = Math.hypot(p.x - other.x, p.y - other.y);
                if (dist < 110) {
                    gc.setGlobalAlpha(1.0 - dist / 110);
                    gc.strokeLine(p.x, p.y, other.x, other.y);
                }
            }
        }
        gc.setGlobalAlpha(1.0);
    }

    public void stop() {
        if (timer != null) timer.stop();
    }

    private static class Particle {
        double x, y, vx, vy;

        Particle(double x, double y) {
            this.x = x;
            this.y = y;
            vx = (Math.random() - 0.5) * 0.6;
            vy = (Math.random() - 0.5) * 0.6;
        }

        void move(double w, double h) {
            x += vx;
            y += vy;
            if (x < 0 || x > w) vx *= -1;
            if (y < 0 || y > h) vy *= -1;
        }
    }
}

