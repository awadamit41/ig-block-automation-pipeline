package com.hygiene.automation;

import org.openqa.selenium.WebDriver;

public class ProfileNavigator {

    private final WebDriver driver;

    public ProfileNavigator(WebDriver driver) {
        this.driver = driver;
    }

    public boolean openProfile(String username) {

        if (username == null || username.isBlank()) {
            System.out.println("Invalid username.");
            return false;
        }

        username = username.trim();

        // Basic username validation
        if (!username.matches("[A-Za-z0-9._]+")) {
            System.out.println(
                    "Invalid username format: " + username
            );
            return false;
        }

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

            System.out.println(
                    "Current URL: " + driver.getCurrentUrl()
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Unable to open profile: @" + username
            );

            e.printStackTrace();

            return false;
        }
    }
}