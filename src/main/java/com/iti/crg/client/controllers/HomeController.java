package com.iti.crg.client.controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.game.managers.LoadRecordManager;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import com.iti.crg.client.infrastructure.remote.ServerConnection;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playEntryAnimations();
        loadHistory();
        addPulseEffect(btnOffline);
        addPulseEffect(btnOnline);

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
        String name = file.getName().replace(".record", "");
        String[] parts = name.split("_vs_");

        String playersText = name;
        String dateText = "Recorded Game";

        if(parts.length >= 2) {
            String p1 = parts[0];
            String p2AndDate = parts[1];
            String p2 = p2AndDate;

            if(p2AndDate.contains("_")) {
                p2 = p2AndDate.substring(0, p2AndDate.lastIndexOf("_"));
            }
            playersText = p1 + " vs " + p2;
        }

        VBox card = new VBox(5);
        card.getStyleClass().add("record-card");

        Label lblPlayers = new Label(playersText);
        lblPlayers.getStyleClass().add("record-players");

        Label lblDate = new Label(dateText);
        lblDate.getStyleClass().add("record-date");
        lblDate.setText(new java.util.Date(file.lastModified()).toString());

        card.getChildren().addAll(lblPlayers, lblDate);

        card.setOnMouseClicked(event -> {
            playRecord(file.getAbsolutePath());
        });

        return card;
    }

    private void playRecord(String filePath) {
        GameRecord record = LoadRecordManager.loadFromStream(filePath);

        if (record != null) {
            Navigator.setLast(View.HOME);

            RecordScreenController controller = Navigator.navigate(View.RECORD_SCREEN);

            if (controller != null) {
                controller.setRecord(record);
            }
        }
    }

    private void playEntryAnimations() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), contentBox);
        tt.setFromY(50);
        tt.setToY(0);

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
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Connect to Server");
        dialog.setHeaderText("Enter Server IP Address");

        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField ipField = new TextField();
        ipField.setPromptText("127.0.0.1");
        ipField.setText("127.0.0.1");

        grid.add(new Label("IP Address:"), 0, 0);
        grid.add(ipField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        javafx.application.Platform.runLater(ipField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return ipField.getText();
            }
            return null;
        });

        final Button btConnect = (Button) dialog.getDialogPane().lookupButton(connectButtonType);
        btConnect.addEventFilter(ActionEvent.ACTION, ae -> {
            String ip = ipField.getText();
            if (!isValidIPv4(ip)) {
                ae.consume();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid IP");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid IPv4 address (e.g., 127.0.0.1 or 192.168.1.5)");
                alert.showAndWait();
            }
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(ip -> {
            ServerConnection.getInstance().setIp(ip);

            boolean isConnected = ServerConnection.getInstance().connect();

            if (isConnected) {
                navigate(View.AUTH);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failed");
                alert.setHeaderText("Cannot reach server");
                alert.setContentText("Could not connect to " + ip + ".\nPlease check if the server is running.");
                alert.showAndWait();
            }
        });
    }

    // Helper method for IP Validation using Regex
    private boolean isValidIPv4(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        // Regex for IPv4
        String zeroTo255 = "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";
        String regex = "^(" + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + ")$";
        return ip.matches(regex);
    }

    
    
}
