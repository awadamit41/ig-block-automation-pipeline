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
            // 1. Load configuration
            // -----------------------------------------

            AppConfig config =
                    new AppConfig("config.properties");

            System.out.println("Configuration loaded.");

            // -----------------------------------------
            // 2. Start Edge
            // -----------------------------------------

            EdgeOptions options = new EdgeOptions();

            String userProfile =
                    System.getProperty("user.home")
                    + "\\"
                    + config.getProfileDirectory();

            options.addArguments(
                    "--user-data-dir=" + userProfile
            );

            System.out.println("Starting Edge...");

            driver = new EdgeDriver(options);

            System.out.println(
                    "Edge started successfully."
            );

            // -----------------------------------------
            // 3. Create components
            // -----------------------------------------

            TargetLoader loader =
                    new TargetLoader(
                            config.getTargetFile()
                    );

            ProfileNavigator navigator =
                    new ProfileNavigator(
                            driver,
                            config.getWaitTimeoutSeconds()
                    );

            ProfileVerifier verifier =
                    new ProfileVerifier(driver);

            ActionManager actionManager =
                    new ActionManager(
                            config.isDryRun()
                    );

            ResultLogger logger =
                    new ResultLogger(
                            config.getLogFile()
                    );

            // -----------------------------------------
            // 4. Load targets
            // -----------------------------------------

            List<String> targets =
                    loader.loadTargets();

            System.out.println();
            System.out.println(
                    "Targets loaded: " + targets.size()
            );

            System.out.println(
                    "Dry-run mode: "
                    + config.isDryRun()
            );

            // -----------------------------------------
            // 5. Process targets
            // -----------------------------------------

            for (String username : targets) {

                System.out.println();
                System.out.println(
                        "Processing @" + username
                );

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

                boolean verified =
                        verifier.verifyProfile(username);

                System.out.println(
                        verified
                                ? "STATUS: PROFILE VERIFIED"
                                : "STATUS: PROFILE NOT VERIFIED"
                );

                ActionResult result =
                        actionManager.process(
                                username,
                                verified
                        );

                System.out.println(
                        "ACTION RESULT: " + result
                );

                logger.log(
                        username,
                        "SUCCESS",
                        verified
                                ? "VERIFIED"
                                : "NOT_VERIFIED",
                        result
                );
            }

            System.out.println();
            System.out.println("==============================================");
            System.out.println(" Processing completed.");
            System.out.println("==============================================");

        } catch (Exception e) {

            System.err.println(
                    "Application failed."
            );

            e.printStackTrace();

        } finally {

            if (driver != null) {
                driver.quit();
                System.out.println("Edge closed.");
            }
        }
    }
}