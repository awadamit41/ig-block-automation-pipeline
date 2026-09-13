package com.hygiene.automation;

import org.openqa.selenium.WebDriver;

public class ProfileVerifier implements Verifier {

    private final WebDriver driver;

    public ProfileVerifier(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public boolean verifyProfile(String username) {

        if (username == null || username.isBlank()) {
            return false;
        }

        String expectedUsername = username.trim()
                .replaceFirst("^@", "")
                .toLowerCase();

        String currentUrl = driver.getCurrentUrl();

        if (currentUrl == null || currentUrl.isBlank()) {
            return false;
        }

        /*
         * Normalize the URL so we can inspect the profile path.
         */
        String normalizedUrl = currentUrl
                .toLowerCase()
                .split("\\?")[0]
                .split("#")[0];

        String expectedPath =
                "/"
                + expectedUsername
                + "/";

        boolean verified = normalizedUrl.endsWith(expectedPath);

        if (verified) {
            System.out.println(
                    "PROFILE VERIFIED: @" + expectedUsername
            );
        } else {
            System.out.println(
                    "PROFILE NOT VERIFIED: @" + expectedUsername
            );

            System.out.println(
                    "Actual URL: " + currentUrl
            );
        }

        return verified;
    }
}