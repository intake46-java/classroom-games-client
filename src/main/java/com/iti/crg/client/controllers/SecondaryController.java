package com.iti.crg.client.controllers;

import java.io.IOException;

import com.iti.crg.client.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}