package com.example.lab2;

import javafx.application.Platform;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class LoginTask implements Runnable {
    private final String email;
    private final String password;
    private final List<User> users;
    private final AttemptTracker tracker;
    private final BlockingQueue<FailedAttempt> queue;
    private final Consumer<LoginResult> onResult;

    public LoginTask(String email, String password,
                     List<User> users, AttemptTracker tracker,
                     BlockingQueue<FailedAttempt> queue,
                     Consumer<LoginResult> onResult) {
        this.email = email;
        this.password = password;
        this.users = users;
        this.tracker = tracker;
        this.queue = queue;
        this.onResult = onResult;
    }

    @Override
    public void run() {
        try {
            BlockCheckThread check = new BlockCheckThread(tracker, email);
            check.start();
            check.join();

            if (check.isAccountBlocked()) {
                sendResult(LoginResult.blocked(
                        tracker.getRemainingSeconds(email)));
                return;
            }

            User matchedUser = null;

            for (User user : users) {
                if (user.getUsername().equals(email)) {
                    matchedUser = user;
                    break;
                }
            }

            // Unknown email: no attempt tracking
            if (matchedUser == null) {
                sendResult(LoginResult.failed(-1));
                return;
            }

            // Correct password
            if (matchedUser.getPassword().equals(password)) {
                tracker.reset(email);
                sendResult(LoginResult.success());
                return;
            }

            // Incorrect password: send event to a worker
            FailedAttempt event = new FailedAttempt(email);
            queue.put(event);

            // Wait for the worker's response
            LoginResult result = event.getReply().take();
            sendResult(result);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendResult(LoginResult result) {
        Platform.runLater(() -> onResult.accept(result));
    }
}