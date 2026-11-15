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

        int n = Integer.parseInt(args.get(0));
        int t = Integer.parseInt(args.get(1));
        AttemptTracker tracker = new AttemptTracker(n,t);
        FailureWorker worker1 = new FailureWorker(queue, tracker);
        FailureWorker worker2 = new FailureWorker(queue, tracker);

        worker1.start();
        worker2.start();

        stage.setScene(scene);
        stage.show();
    }
}
