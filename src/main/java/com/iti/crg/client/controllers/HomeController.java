package com.iti.crg.client.controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.game.managers.LoadRecordManager;
import com.iti.crg.client.domain.usecases.LoginResult;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.iti.crg.client.controllers.utils.Navigator.navigate;

public class HomeController implements Initializable {

    @FXML private Canvas backgroundCanvas;
    @FXML private VBox contentBox;
    @FXML private Button btnOffline;
    @FXML private Button btnOnline;
    @FXML private StackPane rootPane;
    @FXML private Label nameLabel;
    @FXML private VBox historyListContainer;
    Socket mySocket;

    BufferedReader dis;
    PrintStream ps;

    private static final int PARTICLE_COUNT = 40;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Initial Entry Animations
        playEntryAnimations();
        loadHistory();
        // 2. Button Hover Effects
        addPulseEffect(btnOffline);
        addPulseEffect(btnOnline);

        // 3. Start Background Animation
        AnimatedNetworkBackground background = new AnimatedNetworkBackground(rootPane);
    }

    private void loadHistory() {
        File folder = new File("Records");
        if (!folder.exists() || !folder.isDirectory()) {
            Label placeholder = new Label("No records found.");
            placeholder.setStyle("-fx-text-fill: #999; -fx-padding: 10;");
            historyListContainer.getChildren().add(placeholder);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".record"));

        if (files != null && files.length > 0) {
            // Sort by Last Modified (Newest first)
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            for (File file : files) {
                historyListContainer.getChildren().add(createRecordCard(file));
            }
        } else {
            Label placeholder = new Label("No recent matches.");
            placeholder.setStyle("-fx-text-fill: #999; -fx-padding: 10;");
            historyListContainer.getChildren().add(placeholder);
        }
    }

    private VBox createRecordCard(File file) {
        // Parse filename for quick display without loading the whole object yet
        // Filename format: "P1_vs_P2_TIMESTAMP.record"
        String name = file.getName().replace(".record", "");
        String[] parts = name.split("_vs_");

        String playersText = name;
        String dateText = "Recorded Game";

        if(parts.length >= 2) {
            // Cleaning up the string display
            String p1 = parts[0];
            // The rest might contain the date
            String p2AndDate = parts[1];
            String p2 = p2AndDate;

            // Basic parsing logic (adjust based on your exact save format)
            if(p2AndDate.contains("_")) {
                p2 = p2AndDate.substring(0, p2AndDate.lastIndexOf("_"));
            }
            playersText = p1 + " vs " + p2;
        }

        // --- Create UI Nodes ---
        VBox card = new VBox(5);
        card.getStyleClass().add("record-card");

        Label lblPlayers = new Label(playersText);
        lblPlayers.getStyleClass().add("record-players");

        Label lblDate = new Label(dateText); // You could format file.lastModified() to a Date string here
        lblDate.getStyleClass().add("record-date");
        lblDate.setText(new java.util.Date(file.lastModified()).toString());

        card.getChildren().addAll(lblPlayers, lblDate);

        // --- Handle Click ---
        card.setOnMouseClicked(event -> {
            playRecord(file.getAbsolutePath());
        });

        return card;
    }

    private void playRecord(String filePath) {
        // 1. Load the data
        GameRecord record = LoadRecordManager.loadFromStream(filePath);

        if (record != null) {
            // 2. Set "Last View" so Back button works
            Navigator.setLast(View.HOME);

            // 3. Navigate
            RecordScreenController controller = Navigator.navigate(View.RECORD_SCREEN);

            // 4. Pass Data
            if (controller != null) {
                controller.setRecord(record);
            }
        }
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





    public void setData(String name, Socket mySocket, BufferedReader dis, PrintStream ps ) {
        nameLabel.setText(name);
        this.mySocket = mySocket;
        this.dis = dis;
        this.ps = ps;
    }

    @FXML
    private void onOffline(ActionEvent event) {
        navigate(View.OFFLINE_VIEW);
    }

    @FXML
    private void onOnline(ActionEvent event) {
        navigate(View.AUTH);
    }

    
    
}
