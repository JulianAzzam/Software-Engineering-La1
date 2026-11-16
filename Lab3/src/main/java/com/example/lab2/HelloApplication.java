package com.example.lab2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ArrayList<User> users = UsersApp.getUsers();

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );

        Scene scene = new Scene(loader.load());

        HelloController controller = loader.getController();
        controller.setUsers(users);
        BlockingQueue<FailedAttempt> queue = new LinkedBlockingQueue<>();
        List<String> args = getParameters().getRaw();

        Integer n = getValidArgument(args, 0, "n");
        if (n == null) {
            Platform.exit();
            return;
        }

        Integer t = getValidArgument(args, 1, "t");
        if (t == null) {
            Platform.exit();
            return;
        }
        AttemptTracker tracker = new AttemptTracker(n,t);

        FailureWorker worker1 = new FailureWorker(queue, tracker);
        FailureWorker worker2 = new FailureWorker(queue, tracker);

        controller.setLoginDependencies(tracker, queue);

        worker1.start();
        worker2.start();


        stage.setOnCloseRequest(event -> {
            controller.stopCountdown();

            worker1.interrupt();
            worker2.interrupt();
        });


        stage.setScene(scene);
        stage.show();
    }


    private Integer getValidArgument(List<String> args, int index, String name) {

        // Try reading the command-line argument
        if (index < args.size()) {
            try {
                int value = Integer.parseInt(args.get(index));

                if (value >= 1) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Invalid argument - ask manually
            }
        }

        // Ask the user until valid input is entered
        while (true) {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Invalid Argument");
            dialog.setHeaderText("Please enter a valid value for " + name);

            dialog.setContentText("Enter a positive integer (>= 1):");

            Optional<String> result = dialog.showAndWait();

            // User cancelled
            if (!result.isPresent()) {
                return null;
            }

            try {
                int value = Integer.parseInt(
                        result.get().trim());

                if (value >= 1) {
                    return value;
                }

            } catch (NumberFormatException e) {
                // Continue to error message
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(
                    "Please enter an integer greater than 0.");
            alert.showAndWait();
        }
    }

}
