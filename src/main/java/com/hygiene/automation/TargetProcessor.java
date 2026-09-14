package com.hygiene.automation;

public class TargetProcessor {

    private final Navigator navigator;
    private final Verifier verifier;
    private final ActionExecutor actionExecutor;
    private final ResultRecorder recorder;

    public TargetProcessor(
        Navigator navigator,
        Verifier verifier,
        ActionExecutor actionExecutor,
        ResultRecorder recorder
    ) {
        this.navigator = navigator;
        this.verifier = verifier;
        this.actionExecutor = actionExecutor;
        this.recorder = recorder;
    }

    public ProcessingResult process(String username) {

        String normalizedUsername =
            normalizeUsername(username);

        if (normalizedUsername == null) {

            return createFailureResult(
                username,
                ProcessingStatus.SKIPPED,
                ActionResult.SKIPPED,
                "Invalid or blank username."
            );
        }

        try {

            printProcessingHeader(normalizedUsername);

            // ----------------------------------------------
            // Navigation
            // ----------------------------------------------

            boolean opened =
                navigator.openProfile(normalizedUsername);

            if (!opened) {

                return createFailureResult(
                    normalizedUsername,
                    ProcessingStatus.NAVIGATION_FAILED,
                    ActionResult.FAILED,
                    "Profile navigation failed."
                );
            }

            // ----------------------------------------------
            // Verification
            // ----------------------------------------------

            boolean verified =
                verifier.verifyProfile(normalizedUsername);

            if (!verified) {

                return createFailureResult(
                    normalizedUsername,
                    ProcessingStatus.VERIFICATION_FAILED,
                    ActionResult.SKIPPED,
                    "Profile verification failed."
                );
            }

            // ----------------------------------------------
            // Action
            // ----------------------------------------------

            ActionResult actionResult =
                actionExecutor.process(
                    normalizedUsername,
                    true
                );

            ProcessingStatus status =
                actionResult == ActionResult.FAILED
                    ? ProcessingStatus.FAILED
                    : ProcessingStatus.SUCCESS;

            String message =
                actionResult == ActionResult.FAILED
                    ? "Action processing failed."
                    : "Target processed successfully.";

            ProcessingResult result =
                new ProcessingResult(
                    normalizedUsername,
                    status,
                    actionResult,
                    message
                );

            recordSafely(
                normalizedUsername,
                "SUCCESS",
                "VERIFIED",
                actionResult
            );

            return result;

        } catch (Exception e) {

            String message =
                buildExceptionMessage(e);

            recordSafely(
                normalizedUsername,
                "FAILED",
                "FAILED",
                ActionResult.FAILED
            );

            return new ProcessingResult(
                normalizedUsername,
                ProcessingStatus.FAILED,
                ActionResult.FAILED,
                message
            );
        }
    }

    private String normalizeUsername(String username) {

        if (username == null || username.isBlank()) {
            return null;
        }

        String normalized =
            username.trim();

        if (normalized.startsWith("@")) {
            normalized =
                normalized.substring(1);
        }

        if (normalized.isBlank()) {
            return null;
        }

        return normalized;
    }

    private ProcessingResult createFailureResult(
        String username,
        ProcessingStatus status,
        ActionResult actionResult,
        String message
    ) {

        ProcessingResult result =
            new ProcessingResult(
                username,
                status,
                actionResult,
                message
            );

        String navigation =
            status == ProcessingStatus.NAVIGATION_FAILED
                ? "FAILED"
                : "SUCCESS";

        String verification =
            status == ProcessingStatus.VERIFICATION_FAILED
                ? "NOT_VERIFIED"
                : status == ProcessingStatus.NAVIGATION_FAILED
                    ? "NOT_CHECKED"
                    : "NOT_CHECKED";

        recordSafely(
            username,
            navigation,
            verification,
            actionResult
        );

        return result;
    }

    private void recordSafely(
        String username,
        String navigation,
        String verification,
        ActionResult actionResult
    ) {

        try {

            recorder.log(
                username,
                navigation,
                verification,
                actionResult
            );

        } catch (Exception e) {

            System.err.println(
                "WARNING: Unable to record result for @"
                + username
            );

            System.err.println(
                "Recorder error: "
                + buildExceptionMessage(e)
            );
        }
    }

    private String buildExceptionMessage(Exception e) {

        String message =
            e.getMessage();

        if (message == null || message.isBlank()) {
            return e.getClass().getSimpleName()
                + " occurred while processing target.";
        }

        return e.getClass().getSimpleName()
            + ": "
            + message;
    }

    private void printProcessingHeader(String username) {

        System.out.println();
        System.out.println(
            "----------------------------------------------"
        );

        System.out.println(
            "Processing @" + username
        );

        System.out.println(
            "----------------------------------------------"
        );
    }
}