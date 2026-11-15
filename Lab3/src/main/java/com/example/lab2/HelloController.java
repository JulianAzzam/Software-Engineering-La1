package com.example.lab2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HelloController {

    private ArrayList<User> users;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField usernameTF;

    @FXML
    private Label infoLabel;

    @FXML
    void onHelloButtonClick(ActionEvent event) {
        String emailText = usernameTF.getText();
        String passwordText = passwordTF.getText();

        for (User user : users) {
            if (user.getUsername().equals(emailText)
                    && user.getPassword().equals(passwordText)) {

                infoLabel.setText("Login successful!");
                try {
                    // 1. Load the new FXML file
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome-view.fxml")));

                    // 2. Get the current Stage (Window) using the event source
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // 3. Create the new scene with the loaded layout
                    Scene scene = new Scene(root);

                    // 4. Set the scene onto the stage and display it
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        infoLabel.setText("Invalid username or password");
    }



}
