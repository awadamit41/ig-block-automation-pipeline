package com.hygiene.automation;

import java.util.List;

import org.openqa.selenium.WebDriver;

public class Application {

    private final BrowserDriverFactory browserDriverFactory;
    private final String configFilePath;

    public Application() {
        this(
            new EdgeBrowserDriverFactory(),
            "config.properties"
        );
    }

    public Application(
        BrowserDriverFactory browserDriverFactory
    ) {
        this(
            browserDriverFactory,
            "config.properties"
        );
    }

    public Application(
        BrowserDriverFactory browserDriverFactory,
        String configFilePath
    ) {
        if (browserDriverFactory == null) {
            throw new IllegalArgumentException(
                "Browser driver factory cannot be null."
            );
        }

        if (configFilePath == null
            || configFilePath.isBlank()) {
            throw new IllegalArgumentException(
                "Configuration file path cannot be blank."
            );
        }

        this.browserDriverFactory =
            browserDriverFactory;

        this.configFilePath =
            configFilePath.trim();
    }

    public int run(String[] args) {

        WebDriver driver = null;

        try {
            CommandLineOptions commandLineOptions =
                new CommandLineOptions();

            commandLineOptions.parse(args);

            AppConfig config =
                new AppConfig(configFilePath);

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

            int actionDelaySeconds =
                config.getActionDelaySeconds();

            System.out.println();
            System.out.println(
                "Browser: " + browser
            );

            System.out.println(
                "Starting browser..."
            );

            driver =
                browserDriverFactory.create(config);

            System.out.println(
                "Browser started successfully."
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

            ActionDelay actionDelay =
                new ThreadActionDelay();

            BlockActionPerformer blockActionPerformer =
                new BlockActionPerformer(driver, dryRun, actionDelay);

            

            ActionManager actionManager =
                new ActionManager(
                    dryRun,
                    blockActionPerformer,
                    actionDelay,
                    actionDelaySeconds
                );

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
                "Targets loaded: " + targets.size()
            );

            System.out.println(
                "Dry-run mode: " + dryRun
            );

            System.out.println(
                "Action delay: "
                + actionDelaySeconds
                + " seconds"
            );

            ProcessingSummary summary =
                new ProcessingSummary();

            for (String username : targets) {

                ProcessingResult result;

                try {

                    result =
                        processor.process(username);

                } catch (Exception e) {

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
                        "Browser closed."
                    );

                } catch (Exception e) {

                    System.err.println(
                        "Unable to close browser cleanly."
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