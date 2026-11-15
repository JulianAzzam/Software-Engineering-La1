package com.example.lab2;

public class BlockCheckThread extends Thread {
    private final AttemptTracker tracker;
    private final String email;
    private boolean blocked;

    public BlockCheckThread(AttemptTracker tracker, String email) {
        this.tracker = tracker;
        this.email = email;
    }

    @Override
    public void run() {
        blocked = tracker.isBlocked(email);
    }

    public boolean isAccountBlocked() {
        return blocked;
    }
}