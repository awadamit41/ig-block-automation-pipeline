package com.hygiene.automation;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println(" Social Media Hygiene Automation");
        System.out.println("==============================================");

        WebDriver driver = null;

        try {

            // -----------------------------------------
            // 1. Start Microsoft Edge
            // -----------------------------------------

            EdgeOptions options = new EdgeOptions();

            String userProfile =
                    System.getProperty("user.home")
                    + "\\social-media-hygiene-edge-profile";

            options.addArguments(
                    "--user-data-dir=" + userProfile
            );

            System.out.println("Starting Edge...");

            driver = new EdgeDriver(options);

            System.out.println(
                    "Edge started successfully."
            );

            // -----------------------------------------
            // 2. Initialize components
            // -----------------------------------------

            TargetLoader loader =
                    new TargetLoader("targets.csv");

            ProfileNavigator navigator =
                    new ProfileNavigator(driver);

            ProfileVerifier verifier =
                    new ProfileVerifier(driver);

            /*
             * Dry-run mode remains enabled.
             */
            ActionManager actionManager =
                    new ActionManager(true);

            ResultLogger logger =
                    new ResultLogger("logs/results.csv");

            // -----------------------------------------
            // 3. Load targets
            // -----------------------------------------

            List<String> targets =
                    loader.loadTargets();

            System.out.println();
            System.out.println(
                    "Targets loaded: " + targets.size()
            );

            // -----------------------------------------
            // 4. Process targets
            // -----------------------------------------

            for (String username : targets) {

                System.out.println();
                System.out.println(
                        "=============================================="
                );

                System.out.println(
                        "Processing @" + username
                );

                System.out.println(
                        "=============================================="
                );

                // -------------------------------------
                // Navigation
                // -------------------------------------

                boolean opened =
                        navigator.openProfile(username);

                if (!opened) {

                    System.out.println(
                            "STATUS: NAVIGATION FAILED"
                    );

                    logger.log(
                            username,
                            "FAILED",
                            "NOT_CHECKED",
                            ActionResult.FAILED
                    );

                    continue;
                }

                // -------------------------------------
                // Verification
                // -------------------------------------

                boolean verified =
                        verifier.verifyProfile(username);

                if (verified) {

                    System.out.println(
                            "STATUS: PROFILE VERIFIED"
                    );

                } else {

                    System.out.println(
                            "STATUS: PROFILE NOT VERIFIED"
                    );
                }

                // -------------------------------------
                // Dry-run action
                // -------------------------------------

                ActionResult result =
                        actionManager.process(
                                username,
                                verified
                        );

                System.out.println(
                        "ACTION RESULT: " + result
                );

                // -------------------------------------
                // Logging
                // -------------------------------------

                logger.log(
                        username,
                        "SUCCESS",
                        verified
                                ? "VERIFIED"
                                : "NOT_VERIFIED",
                        result
                );
            }

            // -----------------------------------------
            // 5. Complete
            // -----------------------------------------

            System.out.println();
            System.out.println("==============================================");
            System.out.println(" Processing completed.");
            System.out.println(
                    "Results saved to logs/results.csv"
            );
            System.out.println("==============================================");

        } catch (Exception e) {

            System.err.println();
            System.err.println(
                    "Application failed."
            );

            e.printStackTrace();

        } finally {

            if (driver != null) {

                driver.quit();

                System.out.println(
                        "Edge closed."
                );
            }
        }
    }
}