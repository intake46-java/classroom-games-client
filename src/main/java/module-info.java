module com.iti.tictactoe.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.iti.crg.client to javafx.fxml;
    exports com.iti.crg.client;
    exports com.iti.crg.client.controllers;
    opens com.iti.crg.client.controllers to javafx.fxml;
}
