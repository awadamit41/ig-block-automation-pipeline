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

        try {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("Processing @" + username);
            System.out.println("----------------------------------------------");

            boolean opened =
                navigator.openProfile(username);

            if (!opened) {

                ProcessingResult result =
                    new ProcessingResult(
                        username,
                        ProcessingStatus.NAVIGATION_FAILED,
                        ActionResult.FAILED,
                        "Profile navigation failed."
                    );

                recorder.log(
                    username,
                    "FAILED",
                    "NOT_CHECKED",
                    ActionResult.FAILED
                );

                return result;
            }

            boolean verified =
                verifier.verifyProfile(username);

            if (!verified) {

                ProcessingResult result =
                    new ProcessingResult(
                        username,
                        ProcessingStatus.VERIFICATION_FAILED,
                        ActionResult.SKIPPED,
                        "Profile verification failed."
                    );

                recorder.log(
                    username,
                    "SUCCESS",
                    "NOT_VERIFIED",
                    ActionResult.SKIPPED
                );

                return result;
            }

            ActionResult actionResult =
                actionExecutor.process(
                    username,
                    true
                );

            ProcessingResult result =
                new ProcessingResult(
                    username,
                    ProcessingStatus.SUCCESS,
                    actionResult,
                    "Target processed successfully."
                );

            recorder.log(
                username,
                "SUCCESS",
                "VERIFIED",
                actionResult
            );

            return result;

        } catch (Exception e) {

            recorder.log(
                username,
                "FAILED",
                "FAILED",
                ActionResult.FAILED
            );

            return new ProcessingResult(
                username,
                ProcessingStatus.FAILED,
                ActionResult.FAILED,
                e.getMessage()
            );
        }
    }
}