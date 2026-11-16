
package com.example.lab2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class HelloController {

    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Label infoLabel;

    @FXML
    private Button loginButton;

    private ArrayList<User> users;
    private AttemptTracker tracker;
    private BlockingQueue<FailedAttempt> queue;

    private Thread countdownThread;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setLoginDependencies(AttemptTracker tracker, BlockingQueue<FailedAttempt> queue) {
        this.tracker = tracker;
        this.queue = queue;
    }

    @FXML
    void onHelloButtonClick(ActionEvent event) {

        String email = usernameTF.getText().trim();
        String password = passwordTF.getText();

        setControlsDisabled(true);
        infoLabel.setText("Checking credentials...");

        LoginTask task = new LoginTask(email, password, users, tracker, queue, result -> handleLoginResult(email, result));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleLoginResult(String email, LoginResult result) {

        switch (result.getStatus()) {

            case SUCCESS:
                openWelcomeScreen();
                break;

            case FAILED:
                setControlsDisabled(false);

                if (result.getValue() == -1) {
                    infoLabel.setText("Invalid username or password");
                } else {
                    infoLabel.setText("Invalid credentials. " + result.getValue() + " attempts remaining.");
                }
                break;

            case BLOCKED:
                startCountdown(email);
                break;
        }
    }

    private void startCountdown(String email) {

        if (countdownThread != null) {
            countdownThread.interrupt();
        }

        setControlsDisabled(true);

        CountdownTask task = new CountdownTask(
                tracker,
                email,

                seconds -> infoLabel.setText("Account blocked. " + seconds + " seconds remaining"),

                () -> {
                    infoLabel.setText("");
                    setControlsDisabled(false);
                }
        );

        countdownThread = new Thread(task);
        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    private void setControlsDisabled(boolean disabled) {
        usernameTF.setDisable(disabled);
        passwordTF.setDisable(disabled);
        loginButton.setDisable(disabled);
    }

    private void openWelcomeScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("welcome-view.fxml"));

            Stage stage = (Stage) usernameTF.getScene().getWindow();

            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            infoLabel.setText("Could not open welcome screen.");
            setControlsDisabled(false);
        }
    }

    public void stopCountdown() {
        if (countdownThread != null) {
            countdownThread.interrupt();
        }
    }


    @FXML
    private void onSynchronizationTest() {

        Thread testThread = new Thread(() -> {

            try {
                String result = SynchronizationTest.runTest();

                System.out.println(result);

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Synchronization Test");
                    alert.setHeaderText("20-Thread Test Results");
                    alert.setContentText(result);
                    alert.show();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        testThread.setDaemon(true);
        testThread.start();
    }

}
