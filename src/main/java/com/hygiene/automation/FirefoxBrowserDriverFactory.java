package com.hygiene.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxBrowserDriverFactory
    implements BrowserDriverFactory {

    private final WebDriverCreator<FirefoxOptions> driverCreator;

    public FirefoxBrowserDriverFactory() {
        this(FirefoxDriver::new);
    }

    FirefoxBrowserDriverFactory(WebDriverCreator<FirefoxOptions> driverCreator) {
        if (driverCreator == null) {
            throw new IllegalArgumentException(
                "WebDriver creator cannot be null."
            );
        }
        this.driverCreator = driverCreator;
    }

    @Override
    public WebDriver create(AppConfig config) {

        if (config == null) {
            throw new IllegalArgumentException(
                "Application configuration cannot be null."
            );
        }

        FirefoxOptions options = new FirefoxOptions();

        String userProfile =
            System.getProperty("user.home")
            + java.io.File.separator
            + config.getProfileDirectory();

        options.addArguments("-profile", userProfile);

        return driverCreator.create(options);
    }
}