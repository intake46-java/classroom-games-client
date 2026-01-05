module com.iti.tictactoe.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires java.base;

    opens com.iti.crg.client to javafx.fxml, org.junit.platform.commons;
    opens com.iti.crg.client.controllers to javafx.fxml, org.junit.platform.commons;
    opens com.iti.crg.client.domain.entities to org.junit.platform.commons, com.google.gson;
    opens com.iti.crg.client.domain.game.aistrategy to org.junit.platform.commons;
    opens com.iti.crg.client.infrastructure.dto to com.google.gson;

    exports com.iti.crg.client;
    exports com.iti.crg.client.controllers;
    exports com.iti.crg.client.domain.entities;
    exports com.iti.crg.client.domain.game.aistrategy;

}
