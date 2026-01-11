package com.iti.crg.client.controllers.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Navigator {

    private static Stage stage;
    private static final String VIEWS_PACKAGE = "/com/iti/crg/client/";

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void navigate(View view) {
        if (stage == null) {
            System.err.println("Stage not initialized in Navigator!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(VIEWS_PACKAGE + view.getFxmlName()));
            Parent root = loader.load();

            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                Scene scene = new Scene(root);
                stage.setScene(scene);
            }

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML: " + view.getFxmlName());
        }
    }
}