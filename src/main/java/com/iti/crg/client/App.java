package com.iti.crg.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        
//        confirm invitation dialog
//        scene = new Scene(loadFXML("game_invitation"), 320, 130);
//        scene.getStylesheets().add(getClass().getResource("game_invitation.css").toExternalForm());
//        stage.setMinWidth(320);
//        stage.setMaxWidth(320);
//        stage.setMinHeight(160);
//        stage.setMaxHeight(160);
        
        
        scene = new Scene(loadFXML("auth"), 1000, 600);
        scene.getStylesheets().add(getClass().getResource("auth.css").toExternalForm());
        
        
        stage.setScene(scene);
        
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}