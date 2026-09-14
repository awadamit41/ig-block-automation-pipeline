package com.hygiene.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeBrowserDriverFactory
    implements BrowserDriverFactory {

    @Override
    public WebDriver create(AppConfig config) {

        EdgeOptions options =
            new EdgeOptions();

        String userProfile =
            System.getProperty("user.home")
            + "\\"
            + config.getProfileDirectory();

        options.addArguments(
            "--user-data-dir=" + userProfile
        );

        return new EdgeDriver(options);
    }
}