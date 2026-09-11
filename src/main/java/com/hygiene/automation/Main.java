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
            // 1. Start Edge
            // -----------------------------------------

            EdgeOptions options = new EdgeOptions();

            String userProfile =
                    System.getProperty("user.home")
                    + "\\social-media-hygiene-edge-profile";

            options.addArguments(
                    "--user-data-dir=" + userProfile
            );

            driver = new EdgeDriver(options);

            System.out.println("Edge started.");

            // -----------------------------------------
            // 2. Load targets
            // -----------------------------------------

            TargetLoader loader =
                    new TargetLoader("targets.csv");

            List<String> targets =
                    loader.loadTargets();

            System.out.println(
                    "Targets loaded: " + targets.size()
            );

            // -----------------------------------------
            // 3. Create components
            // -----------------------------------------

            ProfileNavigator navigator =
                    new ProfileNavigator(driver);

            ProfileVerifier verifier =
                    new ProfileVerifier(driver);

            // -----------------------------------------
            // 4. Process targets
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
                            "SKIPPED: Could not open profile."
                    );
                    continue;
                }

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

                if (verified) {

                    System.out.println(
                            "STATUS: READY FOR NEXT STAGE"
                    );

                } else {

                    System.out.println(
                            "STATUS: SKIPPED"
                    );
                }

            }

            System.out.println();
            System.out.println(
                    "Profile verification test completed."
            );

            Thread.sleep(3000);

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