package com.example.lab2;

public class UserState {
    private int failureCounter = 0;
    private long blockedUntil = 0;

    public synchronized LoginResult recordFailure(int maxRetries, int lockTime) {
        long now = System.currentTimeMillis();

        if (now < blockedUntil) {
            return LoginResult.blocked(getRemainingSeconds());
        }

        if (blockedUntil > 0) {
            failureCounter = 0;
            blockedUntil = 0;
        }

        failureCounter++;

        if (failureCounter >= maxRetries) {
            blockedUntil = now + lockTime * 1000L;
            return LoginResult.blocked(getRemainingSeconds());
        }

        return LoginResult.failed(maxRetries - failureCounter);
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