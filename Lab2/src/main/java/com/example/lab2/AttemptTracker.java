package com.example.lab2;

import java.util.concurrent.ConcurrentHashMap;

public class AttemptTracker {
    private ConcurrentHashMap<String,UserState> hashMap;
    private int maxRetries;
    private int lockTime;

    public AttemptTracker(int maxRetries, int lockTime) {
        hashMap = new ConcurrentHashMap<>();
        this.maxRetries = maxRetries;
        this.lockTime = lockTime;
    }
}
