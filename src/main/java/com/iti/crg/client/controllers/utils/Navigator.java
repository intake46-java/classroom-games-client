package com.iti.crg.client.controllers.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Navigator {

    private static Stage stage;
    private static final String VIEWS_PACKAGE = "/com/iti/crg/client/";
    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;

    public static void setStage(Stage s) {
        stage = s;
    }

    // UPDATED: Now returns the Controller <T>
    public static <T> T navigate(View view) {
        if (stage == null) {
            System.err.println("Stage not initialized in Navigator!");
            return null;
        }

        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(VIEWS_PACKAGE + view.getFxmlName()));
            Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            stage.setWidth(DEFAULT_WIDTH);
            stage.setHeight(DEFAULT_HEIGHT);
            stage.show();

            // Return the controller so we can pass data to it
            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}