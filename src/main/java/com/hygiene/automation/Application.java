package com.hygiene.automation;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Application {

    public int run(String[] args) {

        WebDriver driver = null;

        try {
            // ----------------------------------------------
            // Command-line options
            // ----------------------------------------------

            CommandLineOptions commandLineOptions =
                new CommandLineOptions();

            commandLineOptions.parse(args);

            // ----------------------------------------------
            // Configuration
            // ----------------------------------------------

            AppConfig config =
                new AppConfig("config.properties");

            String browser =
                config.getBrowser();

            String targetFile =
                config.getTargetFile();

            if (commandLineOptions.hasTargetFileOverride()) {
                targetFile =
                    commandLineOptions.getTargetFile();
            }

            boolean dryRun =
                config.isDryRun();

            if (commandLineOptions.hasDryRunOverride()) {
                dryRun =
                    commandLineOptions.getDryRun();
            }

            // ----------------------------------------------
            // Browser startup
            // ----------------------------------------------

            EdgeOptions options =
                new EdgeOptions();

            String userProfile =
                System.getProperty("user.home")
                + "\\"
                + config.getProfileDirectory();

            options.addArguments(
                "--user-data-dir=" + userProfile
            );

            System.out.println();
            System.out.println(
                "Browser: " + browser
            );

            System.out.println(
                "Starting Edge..."
            );

            driver =
                new EdgeDriver(options);

            System.out.println(
                "Edge started successfully."
            );

            // ----------------------------------------------
            // Application components
            // ----------------------------------------------

            TargetLoader loader =
                new TargetLoader(targetFile);

            ProfileNavigator navigator =
                new ProfileNavigator(
                    driver,
                    config.getWaitTimeoutSeconds()
                );

            ProfileVerifier verifier =
                new ProfileVerifier(driver);

            ActionManager actionManager =
                new ActionManager(dryRun);

            ResultLogger logger =
                new ResultLogger(config.getLogFile());

            TargetProcessor processor =
                new TargetProcessor(
                    navigator,
                    verifier,
                    actionManager,
                    logger
                );

            // ----------------------------------------------
            // Load targets
            // ----------------------------------------------

            List<String> targets =
                loader.loadTargets();

            System.out.println();
            System.out.println(
                "Targets loaded: " + targets.size()
            );

            System.out.println(
                "Dry-run mode: " + dryRun
            );

            // ----------------------------------------------
            // Process targets
            // ----------------------------------------------

            ProcessingSummary summary =
                new ProcessingSummary();

            for (String username : targets) {

                ProcessingResult result;

                try {

                    result =
                        processor.process(username);

                } catch (Exception e) {

                    /*
                     * TargetProcessor is already responsible for
                     * converting expected processing failures into
                     * ProcessingResult objects.
                     *
                     * This outer guard protects the application
                     * from an unexpected processor-level failure
                     * and allows the next target to continue.
                     */

                    System.err.println();
                    System.err.println(
                        "Unexpected error while processing @"
                        + username
                    );

                    System.err.println(
                        e.getClass().getSimpleName()
                        + ": "
                        + e.getMessage()
                    );

                    result =
                        new ProcessingResult(
                            username,
                            ProcessingStatus.FAILED,
                            ActionResult.FAILED,
                            buildExceptionMessage(e)
                        );
                }

                summary.record(result);

                printResult(result);
            }

            // ----------------------------------------------
            // Summary
            // ----------------------------------------------

            summary.printSummary(dryRun);

            System.out.println();
            System.out.println(
                "=============================================="
            );

            System.out.println(
                " Processing completed."
            );

            System.out.println(
                "=============================================="
            );

            return 0;

        } catch (IllegalArgumentException e) {

            System.err.println();
            System.err.println(
                "Configuration or command-line error:"
            );

            System.err.println(
                e.getMessage()
            );

            return 2;

        } catch (Exception e) {

            System.err.println();
            System.err.println(
                "Application failed:"
            );

            System.err.println(
                e.getMessage()
            );

            return 1;

        } finally {

            // ----------------------------------------------
            // Browser shutdown
            // ----------------------------------------------

            if (driver != null) {

                try {

                    driver.quit();

                    System.out.println(
                        "Edge closed."
                    );

                } catch (Exception e) {

                    System.err.println(
                        "Unable to close Edge cleanly."
                    );
                }
            }
        }
    }

    private void printResult(
        ProcessingResult result
    ) {

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

    private String buildExceptionMessage(
        Exception e
    ) {

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
}