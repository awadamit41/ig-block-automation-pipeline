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

            AppConfig config =
                    new AppConfig("config.properties");

            EdgeOptions options =
                    new EdgeOptions();

            String userProfile =
                    System.getProperty("user.home")
                    + "\\"
                    + config.getProfileDirectory();

            options.addArguments(
                    "--user-data-dir=" + userProfile
            );

            System.out.println("Starting Edge...");

            driver =
                    new EdgeDriver(options);

            System.out.println(
                    "Edge started successfully."
            );

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

            TargetProcessor processor =
                    new TargetProcessor(
                            navigator,
                            verifier,
                            actionManager,
                            logger
                    );

            List<String> targets =
                    loader.loadTargets();

            System.out.println();
            System.out.println(
                    "Targets loaded: "
                    + targets.size()
            );

            System.out.println(
                    "Dry-run mode: "
                    + config.isDryRun()
            );

            ProcessingSummary summary =
                new ProcessingSummary();

            for (String username : targets) {

                ProcessingResult result =
                        processor.process(username);
                        
                summary.record(result);

                System.out.println(
                        "STATUS: "
                        + result.getStatus()
                );

                System.out.println(
                        "ACTION: "
                        + result.getActionResult()
                );

                System.out.println(
                        "MESSAGE: "
                        + result.getMessage()
                );
            }
            summary.printSummary(
                config.isDryRun()
            );

            System.out.println();
            System.out.println("==============================================");
            System.out.println(" Processing completed.");
            System.out.println("==============================================");

        } catch (Exception e) {

            System.err.println(
                    "Application startup failed."
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