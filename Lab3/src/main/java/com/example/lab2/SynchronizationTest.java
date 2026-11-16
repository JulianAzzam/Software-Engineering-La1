
package com.example.lab2;

public class SynchronizationTest {

    public static String runTest() throws InterruptedException {

        final int maxRetries = 10000;
        final int threadCount = 20;
        final int attemptsPerThread = 499;

        // Separate tracker for testing only
        AttemptTracker tracker = new AttemptTracker(maxRetries, 30);
        String email = "test@example.com";

        Thread[] threads = new Thread[threadCount];

        // Create and start 20 threads
        for (int i = 0; i < threadCount; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < attemptsPerThread; j++) {
                    tracker.increaseAttempt(email);
                }

            });

            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        int remainingAfterThreads = tracker.getRemainingAttempts(email);

        int actualFailures = maxRetries - remainingAfterThreads;

        // Add another 19 failures
        for (int i = 0; i < 19; i++) {
            tracker.increaseAttempt(email);
        }

        int remainingAfter19 = tracker.getRemainingAttempts(email);

        // The 10,000th failure should block the account
        LoginResult lastResult = tracker.increaseAttempt(email);

        boolean blocked = lastResult.getStatus() == LoginResult.Status.BLOCKED;

        boolean passed = actualFailures == 9980 && remainingAfter19 == 1 && blocked;

        return "Expected failures: 9980\n" +
                "Actual failures: " + actualFailures + "\n" +
                "Remaining after 19 more failures: " +
                remainingAfter19 + "\n" +
                "Blocked on final failure: " + blocked + "\n" +
                "Test result: " + (passed ? "PASSED" : "FAILED");
    }
}
