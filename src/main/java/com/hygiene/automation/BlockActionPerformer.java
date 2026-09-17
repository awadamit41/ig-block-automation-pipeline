package com.hygiene.automation;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BlockActionPerformer {

    // Step 1: the "..." options control on the profile.
    // TODO: verify against the real DOM (see the aria-label you found).
    private static final By OPTIONS_BUTTON =
        By.xpath("//*[@aria-label='Options']/ancestor::div[@role='button'][1]");

    // Step 2: the "Block" row inside the options menu.
    private static final By BLOCK_MENU_ITEM =
        By.xpath("//*[normalize-space()='Block']");

    // Step 3: the confirm dialog's "Block" button. Instagram shows a
    // second element also reading "Block" once the confirm dialog
    // opens. TODO: verify this is really the second match on your
    // account's current DOM — if the menu item disappears before the
    // dialog renders, this index may need to change to [1].
    private static final By CONFIRM_BLOCK_BUTTON =
        By.xpath("(//*[normalize-space()='Block'])[2]");

    // Step 4: the "Dismiss"/"OK" button on the post-block confirmation.
    // TODO: verify actual text — Instagram has used both "Dismiss"
    // and "OK" at different times.
    private static final By DISMISS_BUTTON =
        By.xpath("//*[normalize-space()='Dismiss' or normalize-space()='OK']");

    private static final Duration STEP_TIMEOUT = Duration.ofSeconds(10);
    private static final long POST_ACTION_WAIT_MILLIS = 3000L;

    private final WebDriver driver;
    private final boolean dryRun;

    public BlockActionPerformer(WebDriver driver, boolean dryRun, ActionDelay actionDelay) {

        if (driver == null) {
            throw new IllegalArgumentException(
                "WebDriver cannot be null."
            );
        }

        if (actionDelay == null) {
            throw new IllegalArgumentException(
                "Action delay cannot be null."
            );
        }  

        this.driver = driver;
        this.dryRun = dryRun;
        this.actionDelay = actionDelay;
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

            WebElement optionsButton =
                waitFor(OPTIONS_BUTTON);
            optionsButton.click();

            WebElement blockMenuItem =
                waitFor(BLOCK_MENU_ITEM);
            blockMenuItem.click();

            if (dryRun) {

                System.out.println(
                    "DRY RUN: options menu opened and Block located; "
                    + "confirm click was NOT performed."
                );

                return ActionResult.WOULD_EXECUTE;
            }

            WebElement confirmButton =
                waitFor(CONFIRM_BLOCK_BUTTON);
            confirmButton.click();

            WebElement dismissButton =
                waitFor(DISMISS_BUTTON);
            dismissButton.click();

            System.out.println(
                "BLOCK CONFIRMED for @" + username
            );

            Thread.sleep(POST_ACTION_WAIT_MILLIS);

            return ActionResult.EXECUTED;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println(
                "Block action interrupted for @" + username
            );

            return ActionResult.FAILED;

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

    private WebElement waitFor(By locator) {

        WebDriverWait wait =
            new WebDriverWait(driver, STEP_TIMEOUT);

        return wait.until(
            ExpectedConditions.elementToBeClickable(locator)
        );
    }

    public boolean isBlockControlPresent() {

        return !driver.findElements(BLOCK_MENU_ITEM).isEmpty();
    }
}