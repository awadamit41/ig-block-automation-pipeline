package com.hygiene.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BlockActionPerformer {

    private static final By BLOCK_BUTTON =
        By.xpath("//button[normalize-space()='Block']");

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
         * Real account-changing execution is intentionally
         * disabled in this prototype.
         *
         * The UI locator is kept separately so the project
         * can demonstrate identification of the intended
         * control without performing the account-changing click.
         */

        System.out.println(
            "Real account-changing execution is disabled."
        );

        return ActionResult.SKIPPED;
    }

    public WebElement findBlockControl(int timeoutSeconds) {

        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException(
                "Timeout must be greater than 0."
            );
        }

        WebDriverWait wait =
            new WebDriverWait(
                driver,
                Duration.ofSeconds(timeoutSeconds)
            );

        return wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                BLOCK_BUTTON
            )
        );
    }

    public boolean isBlockControlPresent() {

        List<WebElement> elements =
            driver.findElements(BLOCK_BUTTON);

        return !elements.isEmpty();
    }
}