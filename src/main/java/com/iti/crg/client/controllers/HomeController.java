package com.iti.crg.client.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable {

    @FXML
    private Label nameLabel;
    
    Socket mySocket;

    BufferedReader dis;
    PrintStream ps;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setData(String name,Socket mySocket, BufferedReader dis, PrintStream ps ) {
        nameLabel.setText(name);
        this.mySocket = mySocket;
        this.dis = dis;
        this.ps = ps;
        
    }
    
    
}
