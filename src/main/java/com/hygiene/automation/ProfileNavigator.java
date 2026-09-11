package com.hygiene.automation;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfileNavigator {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProfileNavigator(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean openProfile(String username) {

        if (username == null || username.isBlank()) {
            System.out.println("Invalid username.");
            return false;
        }

        username = username.trim();

        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        if (!username.matches("[A-Za-z0-9._]+")) {
            System.out.println(
                    "Invalid username format: " + username
            );
            return false;
        }

        final String targetUsername = username;
        
        String profileUrl =
                "https://www.instagram.com/"
                + username
                + "/";

        try {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("Opening profile: @" + username);
            System.out.println("----------------------------------------------");

            driver.get(profileUrl);

            /*
             * Wait until navigation actually reaches
             * the expected profile URL.
             */
            wait.until(driver ->
                    normalizeUrl(driver.getCurrentUrl())
                            .endsWith(
                                    "/" + targetUsername.toLowerCase() + "/"
                            )
            );

            System.out.println(
                    "Navigation completed: @" + username
            );

            System.out.println(
                    "Current URL: " + driver.getCurrentUrl()
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Navigation timed out or failed: @"
                    + username
            );

            System.out.println(
                    "Current URL: " + driver.getCurrentUrl()
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