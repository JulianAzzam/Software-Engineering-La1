package com.example.lab2;

public class LoginTask implements Runnable{
    @Override
    public void run() {
        BlockCheckThread check = new BlockCheckThread(tracker, email);
        check.start();
        check.join();

        if (check.isAccountBlocked()) {
            // Reject login without checking the password
        }
    }
}
