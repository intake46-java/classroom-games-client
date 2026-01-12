package com.iti.crg.client.controllers;

import com.iti.crg.client.controllers.utils.AnimatedNetworkBackground;
import com.iti.crg.client.controllers.utils.Navigator;
import com.iti.crg.client.controllers.utils.View;
import com.iti.crg.client.domain.usecases.LoginResult;
import com.iti.crg.client.domain.usecases.LoginUseCase;
import com.iti.crg.client.domain.usecases.RegisterUseCase;
import com.iti.crg.client.infrastructure.repository.AuthRepositoryImp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    @FXML private Canvas backgroundCanvas;
    private static final int PARTICLE_COUNT = 40;
    // --- FXML Injections (Make sure these match fx:id in FXML) ---
    @FXML private TextField usernameField;
    @FXML private StackPane root_pane;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Button loginButton;
    @FXML private Button eyeButton;
    @FXML private StackPane passwordContainer;

    @FXML private Button loginTabBtn;
    @FXML private Button registerTabBtn;
    @FXML private Button actionButton;

    private final RegisterUseCase registerUseCase = new RegisterUseCase();
    private boolean isLoginMode = true;

    // --- Dependencies ---
    private final LoginUseCase loginUseCase = new LoginUseCase(new AuthRepositoryImp());
    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimatedNetworkBackground background = new AnimatedNetworkBackground(root_pane);
        // Initialize the visibility state
        passwordTextField.setVisible(false);

        // Sync the text between the hidden PasswordField and visible TextField
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());



        setLoginMode();
    }

    @FXML
    private void onLoginTabClick(ActionEvent event) {
        setLoginMode();
    }

    @FXML
    private void onRegisterTabClick(ActionEvent event) {
        setRegisterMode();
    }

    @FXML
    private void backOffline(ActionEvent event) {
        Navigator.navigate(View.HOME);
    }
    private void setLoginMode() {
        isLoginMode = true;
        loginTabBtn.getStyleClass().removeAll("inactive-tab");
        loginTabBtn.getStyleClass().add("active-tab");

        registerTabBtn.getStyleClass().removeAll("active-tab");
        registerTabBtn.getStyleClass().add("inactive-tab");

        actionButton.setText("Login");
    }

    private void setRegisterMode() {
        isLoginMode = false;
        registerTabBtn.getStyleClass().removeAll("inactive-tab");
        registerTabBtn.getStyleClass().add("active-tab");

        loginTabBtn.getStyleClass().removeAll("active-tab");
        loginTabBtn.getStyleClass().add("inactive-tab");

        actionButton.setText("Register");
    }




    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
        }
    }

    
    private void navigateToOnlinePlayers(ActionEvent event, String username, LoginResult result) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iti/crg/client/onlinePlayers.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.getScene().getStylesheets().add(getClass().getResource("/com/iti/crg/client/onlinePlayers.css").toExternalForm());
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("System Error", "Could not load Home Screen.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleAuthAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        actionButton.setDisable(true);

        new Thread(() -> {
            LoginResult result;

            if (isLoginMode) {
                // Perform Login
                result = loginUseCase.execute(username, password);
                
            } else {
                // Perform Register
                result = registerUseCase.execute(username, password);
            }

            Platform.runLater(() -> {
                actionButton.setDisable(false);
                
                if (result.isSuccess()) {
                    if (isLoginMode) {
                        OnlineLobbyController.myUsername = username;
                        navigateToOnlinePlayers(event, username, result);
                    } else {
                        // If register success, switch back to login or auto-login
                        showAlert("Success", "Account created! Please log in.");
                        setLoginMode();
                    }
                } else {
                    String msg = isLoginMode ? "Login Failed" : "Registration Failed (Username taken?)";
                    showAlert("Error", msg);
                    actionButton.setDisable(false);
                    
                }
            });
        }).start();
    }
}