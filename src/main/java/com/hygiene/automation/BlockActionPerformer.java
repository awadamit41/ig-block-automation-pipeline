package com.hygiene.automation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
    private static final int POST_ACTION_WAIT_SECONDS = 3;

    private final WebDriver driver;
    private final boolean dryRun;
    private final ActionDelay actionDelay;

    public BlockActionPerformer(
        WebDriver driver,
        boolean dryRun,
        ActionDelay actionDelay
    ) {

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
        System.out.println(
            "Current URL: " + driver.getCurrentUrl()
        );

        try {

            System.out.println("STEP 1: locating Options button...");
            WebElement optionsButton =
                waitFor(OPTIONS_BUTTON, "OPTIONS_BUTTON");
            logElementInfo(optionsButton, "OPTIONS_BUTTON");
            jsClick(optionsButton);
            System.out.println("STEP 1: clicked.");

            System.out.println("STEP 2: locating Block menu item...");
            WebElement blockMenuItem =
                waitFor(BLOCK_MENU_ITEM, "BLOCK_MENU_ITEM");
            logElementInfo(blockMenuItem, "BLOCK_MENU_ITEM");
            jsClick(blockMenuItem);
            System.out.println("STEP 2: clicked.");

            System.out.println(
                "Elements matching 'Block' right now: "
                + driver.findElements(BLOCK_MENU_ITEM).size()
            );
            captureScreenshot(username, "after-step2");

            if (dryRun) {

                System.out.println(
                    "DRY RUN: options menu opened and Block located; "
                    + "confirm click was NOT performed."
                );

                return ActionResult.WOULD_EXECUTE;
            }

            System.out.println("STEP 3: locating confirm Block button...");
            WebElement confirmButton =
                waitFor(CONFIRM_BLOCK_BUTTON, "CONFIRM_BLOCK_BUTTON");
            logElementInfo(confirmButton, "CONFIRM_BLOCK_BUTTON");
            jsClick(confirmButton);
            System.out.println("STEP 3: clicked.");

            System.out.println("STEP 4: locating Dismiss button...");
            WebElement dismissButton =
                waitFor(DISMISS_BUTTON, "DISMISS_BUTTON");
            logElementInfo(dismissButton, "DISMISS_BUTTON");
            jsClick(dismissButton);
            System.out.println("STEP 4: clicked.");

            System.out.println(
                "BLOCK CONFIRMED for @" + username
            );

            actionDelay.waitBeforeAction(POST_ACTION_WAIT_SECONDS);

            return ActionResult.EXECUTED;

        } catch (Exception e) {

            System.err.println(
                "Unable to complete block action for @" + username
            );
            System.err.println(
                e.getClass().getSimpleName() + ": " + e.getMessage()
            );
            System.err.println(
                "URL at failure: " + driver.getCurrentUrl()
            );

            captureScreenshot(username, "on-failure");
            dumpPageSourceSnippet();

            return ActionResult.FAILED;
        }
    }

    private WebElement waitFor(By locator, String label) {

        WebDriverWait wait =
            new WebDriverWait(driver, STEP_TIMEOUT);

        try {
            return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
            );
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println(
                "FAILED at " + label + " — locator never became "
                + "clickable: " + locator
            );
            throw e;
        }
    }

    private void captureScreenshot(String username, String label) {

        try {
            byte[] pngBytes =
                ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Path dir = Path.of("logs", "screenshots");
            Files.createDirectories(dir);

            String safeUsername = username.replaceAll("[^a-zA-Z0-9._-]", "_");
            String fileName =
                safeUsername + "-" + label + "-"
                + System.currentTimeMillis() + ".png";

            Path target = dir.resolve(fileName);
            Files.write(target, pngBytes);

            System.out.println(
                "Screenshot saved: " + target.toAbsolutePath()
            );

        } catch (Exception e) {
            System.err.println(
                "Could not capture screenshot: " + e.getMessage()
            );
        }
    }

    private void dumpPageSourceSnippet() {

        try {
            String source = driver.getPageSource();
            int limit = Math.min(source.length(), 2000);

            System.err.println(
                "--- Page source snippet (first "
                + limit + " chars) ---"
            );
            System.err.println(source.substring(0, limit));
            System.err.println("--- end snippet ---");

        } catch (Exception e) {
            System.err.println(
                "Could not capture page source: " + e.getMessage()
            );
        }
    }

    private void jsClick(WebElement element) {

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block: 'center'}); "
            + "arguments[0].click();",
            element
        );
    }

    private void logElementInfo(WebElement element, String label) {

        try {
            System.out.println(
                label + " -> tag="
                + element.getTagName()
                + " text='" + element.getText() + "'"
                + " size=" + element.getSize()
                + " location=" + element.getLocation()
            );
        } catch (Exception e) {
            System.out.println(
                label + " -> could not read element info: "
                + e.getMessage()
            );
        }
    }

    public boolean isBlockControlPresent() {

        return !driver.findElements(BLOCK_MENU_ITEM).isEmpty();
    }
}