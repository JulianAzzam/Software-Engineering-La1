package com.example.lab2;

public class UserState {
    private int failureCounter = 0;
    private long blockedUntil = 0;

    public synchronized void recordFailure(int maxRetries, int lockTime) {
        long now = System.currentTimeMillis();

        // Ignore failures during blocking
        if (now < blockedUntil) {
            return;
        }

        // Reset after blocking expires
        if (blockedUntil > 0) {
            failureCounter = 0;
            blockedUntil = 0;
        }

        failureCounter++;

        // Block after reaching the maximum failures
        if (failureCounter >= maxRetries) {
            blockedUntil = now + lockTime * 1000L;
        }
    }

    public synchronized boolean isBlocked() {
        return System.currentTimeMillis() < blockedUntil;
    }

    public synchronized void reset() {
        failureCounter = 0;
        blockedUntil = 0;
    }

    public synchronized int getFailureCounter() {
        return failureCounter;
    }

    public synchronized long getRemainingSeconds() {
        long remaining = blockedUntil - System.currentTimeMillis();
        return Math.max(0, (remaining + 999) / 1000);
    }
}