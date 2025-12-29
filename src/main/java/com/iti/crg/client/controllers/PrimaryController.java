package com.iti.crg.client.controllers;

import java.io.IOException;

import com.iti.crg.client.App;
import javafx.fxml.FXML;

public class PrimaryController {
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
