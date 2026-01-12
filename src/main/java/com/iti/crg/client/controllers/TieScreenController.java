package com.iti.crg.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class TieScreenController {

    @FXML private MediaView mediaView;
    @FXML private VBox placeholderBox;
    @FXML private Button playAgainButton;
    @FXML private Button backToHomeButton;

    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        loadTieVideo();
        mediaView.setOnMouseClicked(event -> {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        }
    });


    }

private void loadTieVideo() {
    try {
        String videoPath = getClass().getResource("/videos/tie.mp4").toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setSmooth(true);

        mediaPlayer.setOnReady(() -> {
            placeholderBox.setVisible(false);
            mediaView.setVisible(true);
            mediaPlayer.play(); // play once
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            System.out.println("Tie video finished.");
            mediaPlayer.stop();
        });

        mediaPlayer.setOnError(() ->
            System.err.println("Media error: " + mediaPlayer.getError().getMessage())
        );

        media.setOnError(() ->
            System.err.println("Media loading error: " + media.getError().getMessage())
        );

    } catch (NullPointerException e) {
        System.err.println("Video file not found at /videos/tie.mp4");
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Could not load video: " + e.getMessage());
        e.printStackTrace();
    }
}
    @FXML
    private void handlePlayAgain() {
        // Navigation logic to restart game
        // Example: App.setRoot("gameBoard");
    }

    @FXML
    private void handleBackToHome() {
        // Navigation logic to go back home
        // Example: App.setRoot("home");
    }

    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    public void cleanup() {
        stopVideo();
    }
}