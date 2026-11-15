package com.example.lab2;

public class LoginResult {
    public enum Status {
        SUCCESS, FAILED, BLOCKED
    }

    private final Status status;
    private final long value;

    private LoginResult(Status status, long value) {
        this.status = status;
        this.value = value;
    }

    public static LoginResult success() {
        return new LoginResult(Status.SUCCESS, 0);
    }

    public static LoginResult failed(int remainingAttempts) {
        return new LoginResult(Status.FAILED, remainingAttempts);
    }

    public static LoginResult blocked(long remainingSeconds) {
        return new LoginResult(Status.BLOCKED, remainingSeconds);
    }

    public Status getStatus() {
        return status;
    }

    public long getValue() {
        return value;
    }
}