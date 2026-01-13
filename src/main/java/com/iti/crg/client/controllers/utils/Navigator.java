package com.iti.crg.client.controllers.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Navigator {

    private static Stage stage;
    private static View lastView;
    private static final String VIEWS_PACKAGE = "/com/iti/crg/client/";

    // Defaults for the initial launch
    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;

    public static void setStage(Stage s) {
        stage = s;
    }


    @SuppressWarnings("unchecked")
    public static <T> T navigate(View view) {
        if (stage == null) {
            System.err.println("FATAL: Stage not initialized in Navigator. Call Navigator.setStage() first.");
            return null;
        }

        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(VIEWS_PACKAGE + view.getFxmlName()));
            Parent root = loader.load();

            Scene scene = stage.getScene();

            if (scene == null) {
                // FIRST LAUNCH: Create scene and set default dimensions
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setWidth(DEFAULT_WIDTH);
                stage.setHeight(DEFAULT_HEIGHT);
            } else {
                // SUBSEQUENT NAVIGATIONS: Just replace the root (content)
                // This keeps the window size and position exactly where the user left it.
                scene.setRoot(root);
            }

            stage.show();

            // Return the controller typed as T
            return (T) loader.getController();

        } catch (IOException e) {
            System.err.println("Failed to navigate to: " + view.getFxmlName());
            e.printStackTrace();
            return null;
        }
    }


    public static void setLast(View view) {
        lastView = view;
    }


    public static void navigateBack() {
        if (lastView != null) {
            navigate(lastView);
        } else {
            System.err.println("Warning: navigateBack() called, but no previous view is set.");
        }
    }
}