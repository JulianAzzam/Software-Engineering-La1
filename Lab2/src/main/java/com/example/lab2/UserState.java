package com.example.lab2;

public class UserState {
    private String email;
    private int failureCounter;

    private final Object mutex = new Object();

    public void increment() {
        synchronized (mutex) { // Acquires the mutex
            failureCounter++;
        } // Automatically releases the mutex here, even if an exception occurs
    }

}
