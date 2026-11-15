package com.example.lab2;

import java.util.concurrent.ConcurrentHashMap;

public class AttemptTracker {
    private final ConcurrentHashMap<String, UserState> hashMap;
    private final int maxRetries;
    private final int lockTime;

    public AttemptTracker(int maxRetries, int lockTime) {
        if (maxRetries < 1 || lockTime < 1)
            throw new IllegalArgumentException();

        hashMap = new ConcurrentHashMap<>();
        this.maxRetries = maxRetries;
        this.lockTime = lockTime;
    }

    public void increaseAttempt(String email) {
        UserState user = hashMap.computeIfAbsent(
                email, k -> new UserState()
        );

        user.recordFailure(maxRetries, lockTime);
    }
    public boolean isBlocked(String email) {
        UserState user = hashMap.get(email);
        return user != null && user.isBlocked();
    }

    public long getRemainingSeconds(String email) {
        UserState user = hashMap.get(email);
        return user == null ? 0 : user.getRemainingSeconds();
    }

    public int getRemainingAttempts(String email) {
        UserState user = hashMap.get(email);
        return user == null ? maxRetries
                : Math.max(0, maxRetries - user.getFailureCounter());
    }

    public void reset(String email) {
        UserState user = hashMap.get(email);
        if (user != null) {
            user.reset();
        }
    }
}
