package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.ScoreManager;
import com.iti.crg.client.controllers.utils.View;
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
            mediaView.setOnMouseClicked(event -> {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        }
    });

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
                mediaPlayer.play(); // play once
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Lose video finished. Click to replay if desired.");
                mediaPlayer.stop();
            });

            mediaPlayer.setOnError(() ->
                System.err.println("Media error: " + mediaPlayer.getError().getMessage())
            );

            media.setOnError(() ->
                System.err.println("Media loading error: " + media.getError().getMessage())
            );

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

        TicTacToeController controller = Navigator.navigate(View.GAME_BOARD);

        if (controller == null) return;

        if (ScoreManager.isSinglePlayerMode()) {

            controller.initComputerGame(
                    ScoreManager.getP1Name(),
                    ScoreManager.getDifficulty(),
                    true
            );

        } else {
            controller.initLocalTwoPlayerGame(
                    ScoreManager.getP1Name(),
                    ScoreManager.getP2Name()
            );
        }
    }
    
    @FXML
    private void handleBackToHome() {
        Navigator.navigateBack();
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