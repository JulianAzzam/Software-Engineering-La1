package com.example.lab2;

import java.util.concurrent.BlockingQueue;

public class FailureWorker extends Thread {
    private final BlockingQueue<FailedAttempt> queue;
    private final AttemptTracker tracker;

    public FailureWorker(BlockingQueue<FailedAttempt> queue,
                         AttemptTracker tracker) {
        this.queue = queue;
        this.tracker = tracker;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                FailedAttempt event = queue.take();

                LoginResult result =
                        tracker.increaseAttempt(event.getEmail());

                event.getReply().put(result);
            }
        } catch (InterruptedException e) {
            interrupt();
        }
    }
}