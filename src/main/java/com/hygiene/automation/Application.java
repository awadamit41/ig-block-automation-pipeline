package com.hygiene.automation;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Application {

    public int run(String[] args) {

        WebDriver driver = null;

        try {
            CommandLineOptions commandLineOptions =
                new CommandLineOptions();

            commandLineOptions.parse(args);

            AppConfig config =
                new AppConfig("config.properties");

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

            List<String> targets =
                loader.loadTargets();

            System.out.println();
            System.out.println(
                "Targets loaded: "
                + targets.size()
            );

            System.out.println(
                "Dry-run mode: "
                + dryRun
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
}