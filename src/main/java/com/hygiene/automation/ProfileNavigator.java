package com.hygiene.automation;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfileNavigator {

    private final WebDriver driver;
    private final WebDriverWait wait;

public ProfileNavigator(
        WebDriver driver,
        int timeoutSeconds
) {
    this.driver = driver;
    this.wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(timeoutSeconds)
    );
}

    public boolean openProfile(String username) {

        if (username == null || username.isBlank()) {
            System.out.println("Invalid username.");
            return false;
        }

        String targetUsername = username.trim();

        if (targetUsername.startsWith("@")) {
            targetUsername = targetUsername.substring(1);
        }

        if (!targetUsername.matches("[A-Za-z0-9._]+")) {
            System.out.println(
                    "Invalid username format: " + targetUsername
            );
            return false;
        }

        final String normalizedUsername =
                targetUsername.toLowerCase();

        String profileUrl =
                "https://www.instagram.com/"
                + targetUsername
                + "/";

        try {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println(
                    "Opening profile: @" + targetUsername
            );
            System.out.println("----------------------------------------------");

            driver.get(profileUrl);

            wait.until(currentDriver ->
                    normalizeUrl(currentDriver.getCurrentUrl())
                            .endsWith(
                                    "/" + normalizedUsername + "/"
                            )
            );

            System.out.println(
                    "Navigation completed: @"
                    + targetUsername
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Navigation timed out or failed: @"
                    + targetUsername
            );

            System.out.println(
                    "Current URL: "
                    + driver.getCurrentUrl()
            );

            return false;
        }
    }

    private String normalizeUrl(String url) {

        if (url == null) {
            return "";
        }

        return url
                .toLowerCase()
                .split("\\?")[0]
                .split("#")[0];
    }
}