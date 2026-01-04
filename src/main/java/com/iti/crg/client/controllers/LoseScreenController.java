package com.iti.crg.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class LoseScreenController {
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private VBox placeholderBox;
    
    @FXML
    private Button playAgainButton;
    
    @FXML
    private Button backToHomeButton;
    
    private MediaPlayer mediaPlayer;
    
    @FXML
    public void initialize() {
        loadLoseVideo();
    }
    
private void loadLoseVideo() {
    try {
        String videoPath = getClass().getResource("/videos/lose.mp4").toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);

        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.setSmooth(true);

            mediaPlayer.setOnReady(() -> {
                placeholderBox.setVisible(false);
                mediaView.setVisible(true);
                Duration totalDuration = media.getDuration();
                Duration loopPoint = totalDuration.subtract(Duration.millis(100)); 
                mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    if (newTime.greaterThanOrEqualTo(loopPoint)) {
                        mediaPlayer.seek(Duration.ZERO);
                    }
                });
                
                mediaPlayer.play();
            });
        mediaPlayer.setOnError(() -> {
            System.err.println("Media error: " + mediaPlayer.getError().getMessage());
        });

        media.setOnError(() -> {
            System.err.println("Media loading error: " + media.getError().getMessage());
        });

    } catch (NullPointerException e) {
        System.err.println("Video file not found at /videos/lose.mp4");
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Could not load video: " + e.getMessage());
        e.printStackTrace();
    }
}    
    @FXML
    private void handlePlayAgain() {
        // Add your navigation logic here
        // Example: App.setRoot("game");
    }
    
    @FXML
    private void handleBackToHome() {
        // Add your navigation logic here
        // Example: App.setRoot("menu");
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