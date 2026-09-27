package com.hygiene.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBrowserDriverFactory
    implements BrowserDriverFactory {

    private final WebDriverCreator<ChromeOptions> driverCreator;

    public ChromeBrowserDriverFactory() {
        this(ChromeDriver::new);
    }

    ChromeBrowserDriverFactory(WebDriverCreator<ChromeOptions> driverCreator) {
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

        ChromeOptions options = new ChromeOptions();

        String userProfile =
            System.getProperty("user.home")
            + java.io.File.separator
            + config.getProfileDirectory();

        options.addArguments("--user-data-dir=" + userProfile);

        return driverCreator.create(options);
    }
}