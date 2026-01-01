module com.iti.tictactoe.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.iti.crg.client to javafx.fxml, org.junit.platform.commons;
    opens com.iti.crg.client.controllers to javafx.fxml, org.junit.platform.commons;
    opens com.iti.crg.client.domain.entities to org.junit.platform.commons;
    opens com.iti.crg.client.domain.game.aistrategy to org.junit.platform.commons;


    exports com.iti.crg.client;
    exports com.iti.crg.client.controllers;
    exports com.iti.crg.client.domain.entities;
    exports com.iti.crg.client.domain.game.aistrategy;

}
