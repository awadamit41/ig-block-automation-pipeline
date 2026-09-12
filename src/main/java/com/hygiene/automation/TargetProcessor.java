package com.hygiene.automation;

public class TargetProcessor {

    private final ProfileNavigator navigator;
    private final ProfileVerifier verifier;
    private final ActionManager actionManager;
    private final ResultLogger logger;

    public TargetProcessor(
            ProfileNavigator navigator,
            ProfileVerifier verifier,
            ActionManager actionManager,
            ResultLogger logger
    ) {
        this.navigator = navigator;
        this.verifier = verifier;
        this.actionManager = actionManager;
        this.logger = logger;
    }

    public ProcessingResult process(String username) {

        try {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("Processing @" + username);
            System.out.println("----------------------------------------------");

            // -----------------------------------------
            // Navigation
            // -----------------------------------------

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

                logger.log(
                        username,
                        "FAILED",
                        "NOT_CHECKED",
                        ActionResult.FAILED
                );

                return result;
            }

            // -----------------------------------------
            // Verification
            // -----------------------------------------

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

                logger.log(
                        username,
                        "SUCCESS",
                        "NOT_VERIFIED",
                        ActionResult.SKIPPED
                );

                return result;
            }

            // -----------------------------------------
            // Action decision
            // -----------------------------------------

            ActionResult actionResult =
                    actionManager.process(
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

            logger.log(
                    username,
                    "SUCCESS",
                    "VERIFIED",
                    actionResult
            );

            return result;

        } catch (Exception e) {

            logger.log(
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