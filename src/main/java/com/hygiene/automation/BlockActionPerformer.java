package com.hygiene.automation;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BlockActionPerformer {

    private static final By BLOCK_BUTTON =
        By.xpath("//button[normalize-space()='Block']");

    private final WebDriver driver;
    private final boolean dryRun;

    public BlockActionPerformer(WebDriver driver, boolean dryRun) {

        if (driver == null) {
            throw new IllegalArgumentException(
                "WebDriver cannot be null."
            );
        }

        this.driver = driver;
        this.dryRun = dryRun;
    }

    public ActionResult block(String username) {

        if (username == null || username.isBlank()) {
            return ActionResult.SKIPPED;
        }

        System.out.println();
        System.out.println(
            "BLOCK ACTION REQUESTED: @" + username
        );

        try {

            WebElement blockButton =
                findBlockControl(10);

            System.out.println(
                "Block control located for @" + username
            );
            System.out.println(
                "Block control text: " + blockButton.getText()
            );
            System.out.println(
                "Block control is displayed: " + blockButton.isDisplayed()
            );
            System.out.println(
                "Block control is enabled: " + blockButton.isEnabled()
            );

            if (dryRun) {

                System.out.println(
                    "DRY RUN: account-changing click was NOT performed."
                );

                return ActionResult.WOULD_EXECUTE;
            }

            blockButton.click();

            System.out.println(
                "BLOCK CONFIRMED for @" + username
            );

            return ActionResult.EXECUTED;

        } catch (Exception e) {

            System.err.println(
                "Unable to complete block action for @" + username
            );

            System.err.println(
                e.getClass().getSimpleName() + ": " + e.getMessage()
            );

            return ActionResult.FAILED;
        }
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
            ExpectedConditions.elementToBeClickable(BLOCK_BUTTON)
        );
    }

    public boolean isBlockControlPresent() {

        return !driver.findElements(BLOCK_BUTTON).isEmpty();
    }
}