package com.iti.crg.client.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void login(ActionEvent event) {
            Runnable loginTask = () -> {
                try {
                    Socket mySocket = new Socket(InetAddress.getLocalHost(), 5005);

                    BufferedReader dis = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                    PrintStream ps = new PrintStream(mySocket.getOutputStream());

                    ps.println(username.getText());
                    ps.println(password.getText());
                    ps.flush();

//                    dis.readLine();
                    boolean isPlyerExist = Boolean.parseBoolean(dis.readLine());
                    System.out.println("the player exist " + isPlyerExist);
                    if(isPlyerExist){
//                        String usernameString = dis.readLine();
//                        System.out.println("the username is " + usernameString);
//                        System.out.println("the thread  = " + Thread.currentThread().getName() );

                        Platform.runLater(() -> {
                            System.out.println("i am in run later");
                            try {
                                System.out.println("1");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/home.fxml"));
                                System.out.println("-----------"+getClass().getResource("home.fxml"));
                                System.out.println("2");
                                Parent root = loader.load();
                                System.out.println("3");
                                // Get controller
                                HomeController controller = loader.getController();
                                // Pass data
                                System.out.println("4");
                                controller.setData(username.getText(), mySocket, dis, ps);
                                System.out.println("5");
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                                System.out.println("Login Successful");
                            } catch (IOException ex) {
//                                ex.printStackTrace();
                                System.out.println("IO Exception");
                                
                            }
                         });
                    }
                    else{
                        System.out.println("wrong username or password");
                    }
//                    mySocket.close();

                } catch (IOException e) {
                    System.out.println("The server is off");
                }
            };
            new Thread(loginTask).start();
    }
    
}
