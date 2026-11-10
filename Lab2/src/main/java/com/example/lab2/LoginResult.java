package com.example.lab2;

import java.util.concurrent.ArrayBlockingQueue;

public class LoginResult {
    ArrayBlockingQueue<UserState> blockingQueue;

    public LoginResult() {
        this.blockingQueue = new ArrayBlockingQueue(1);
    }
}
