package com.hygiene.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeBrowserDriverFactory
    implements BrowserDriverFactory {

    private final WebDriverCreator driverCreator;

    public EdgeBrowserDriverFactory() {
        this(EdgeDriver::new);
    }

    EdgeBrowserDriverFactory(
        WebDriverCreator driverCreator
    ) {
        if (driverCreator == null) {
            throw new IllegalArgumentException(
                "WebDriver creator cannot be null."
            );
        }

        this.driverCreator =
            driverCreator;
    }

    @Override
    public WebDriver create(AppConfig config) {

        if (config == null) {
            throw new IllegalArgumentException(
                "Application configuration cannot be null."
            );
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

        return driverCreator.create(options);
    }
}