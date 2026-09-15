package com.hygiene.automation;

public class ThreadActionDelay implements ActionDelay {

    @Override
    public void waitBeforeAction(int seconds) {

        if (seconds < 0) {
            throw new IllegalArgumentException(
                "Delay cannot be negative."
            );
        }

        if (seconds == 0) {
            return;
        }

        System.out.println();
        System.out.println(
            "Waiting " + seconds
            + " seconds before action..."
        );

        try {

            Thread.sleep(seconds * 1000L);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                "Action delay was interrupted.",
                e
            );
        }
    }
}