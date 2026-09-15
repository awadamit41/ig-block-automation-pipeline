package com.hygiene.automation;

import org.openqa.selenium.WebDriver;

public class BlockActionPerformer {

    private final WebDriver driver;

    public BlockActionPerformer(WebDriver driver) {

        if (driver == null) {
            throw new IllegalArgumentException(
                "WebDriver cannot be null."
            );
        }

        this.driver = driver;
    }

    public ActionResult block(String username) {

        if (username == null || username.isBlank()) {
            return ActionResult.SKIPPED;
        }

        System.out.println();
        System.out.println(
            "BLOCK ACTION REQUESTED: @" + username
        );

        /*
         * Real account-changing Selenium interaction is intentionally
         * disabled in this prototype.
         *
         * This class isolates the action boundary so the rest of the
         * application does not depend on Selenium-specific action logic.
         */

        System.out.println(
            "Real account-changing execution is disabled."
        );

        return ActionResult.SKIPPED;
    }
}