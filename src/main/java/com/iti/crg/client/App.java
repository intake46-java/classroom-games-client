package com.iti.crg.client;

import com.iti.crg.client.controllers.RecordScreenController;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.domain.entities.GameRecord;
import com.iti.crg.client.domain.entities.Move;
import static com.iti.crg.client.domain.game.managers.LoadRecordManager.loadFromStream;

import com.iti.crg.client.infrastructure.remote.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        //confirm invitation dialog
//        scene = new Scene(loadFXML("game_invitation"), 320, 130);
//        scene.getStylesheets().add(getClass().getResource("game_invitation.css").toExternalForm());
//        stage.setMinWidth(320);
//        stage.setMaxWidth(320);
//        stage.setMinHeight(160);
//        stage.setMaxHeight(160);
        ////////////////////////////////////////////////////////////////

        //OnlinePlayersController
//        scene = new Scene(loadFXML("onlinePlayers"), 1000, 600);
//        scene.getStylesheets().add(getClass().getResource("onlinePlayers.css").toExternalForm());
//        stage.setMinWidth(1000);
//        stage.setMaxWidth(1000);
//        stage.setMinHeight(600);
//        stage.setMaxHeight(600);
        
////////////////////////////////////////////////////////////////
        scene = new Scene(loadFXML("home"), 1000, 650);
      //  scene.getStylesheets().add(getClass().getResource("gameBoard.css").toExternalForm());

                

        //scene = new Scene(loadFXML("auth"), 1000, 600);
       // scene.getStylesheets().add(getClass().getResource("auth.css").toExternalForm());
       // scene = new Scene(loadFXML("home"), 1000, 600);
       // stage.setScene(scene);
       // Navigator.setStage(stage);
       // stage.show();
       
//       FXMLLoader loader = new FXMLLoader(App.class.getResource("RecordScreen.fxml"));
//        Parent root = loader.load();

        // 2. Get the Controller instance so we can pass data to it
         // RecordScreenController controller = loader.getController();

        // 3. Create Dummy Moves (Simulate a game where X wins diagonally)
        // Assumption: Your Move constructor is Move(row, col, character)
        Move[] dummyMoves = new Move[] {
            new Move(0, 0, 'X'), // Move 1: X top-left
            new Move(0, 1, 'O'), // Move 2: O top-center
            new Move(1, 1, 'X'), // Move 3: X center
            new Move(0, 2, 'O'), // Move 4: O top-right
            new Move(2, 2, 'X')  // Move 5: X bottom-right (WIN)
        };

        // 4. Create the GameRecord object
       // GameRecord dummyRecord = new GameRecord("Osama", "Rashed", dummyMoves);

        // 5. Inject the record into the controller
        // controller.setRecord(dummyRecord);

        // 6. Set the scene and show
      //  scene = new Scene(root, 700, 600); // Adjust width/height as needed
        stage.setOnCloseRequest(event -> {
            System.out.println("Closing application...");

            // 1. Close Server Connection
            try {
                // Check if instance exists to avoid NullPointer if never connected
                ServerConnection.getInstance().forceDisconnect();
            } catch (Exception e) {
                System.err.println("Error disconnecting from server: " + e.getMessage());
            }

            // 2. Stop JavaFX Thread
            Platform.exit();

            // 3. Force Kill (Ensures all background threads/sockets die)
            System.exit(0);
        });

        Navigator.setStage(stage); // Ensure navigator knows the stage if used elsewhere
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml+".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
