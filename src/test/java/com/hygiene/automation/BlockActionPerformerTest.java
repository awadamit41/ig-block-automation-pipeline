package com.hygiene.automation;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class BlockActionPerformerTest {

    private static final By OPTIONS_BUTTON =
        By.xpath("//*[@aria-label='Options']/ancestor::div[@role='button'][1]");

    private static final By BLOCK_MENU_ITEM =
        By.xpath("//*[normalize-space()='Block']");

    private static final By CONFIRM_BLOCK_BUTTON =
        By.xpath("(//*[normalize-space()='Block'])[2]");

    private static final By DISMISS_BUTTON =
        By.xpath("//*[normalize-space()='Dismiss' or normalize-space()='OK']");

    private static class FakeActionDelay implements ActionDelay {

        private int callCount = 0;

        @Override
        public void waitBeforeAction(int seconds) {
            callCount++;
        }

        int getCallCount() {
            return callCount;
        }
    }

    private static void makeClickable(WebElement element) {
        when(element.isDisplayed()).thenReturn(true);
        when(element.isEnabled()).thenReturn(true);
    }

    @Test
    void shouldRejectNullDriver() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new BlockActionPerformer(null, true, new FakeActionDelay())
        );
    }

    @Test
    void shouldRejectNullActionDelay() {
        WebDriver driver = mock(WebDriver.class);
        assertThrows(
            IllegalArgumentException.class,
            () -> new BlockActionPerformer(driver, true, null)
        );
    }

    @Test
    void shouldSkipBlankUsername() {
        WebDriver driver = mock(WebDriver.class);
        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true, new FakeActionDelay());
        assertEquals(ActionResult.SKIPPED, performer.block(""));
    }

    @Test
    void shouldSkipNullUsername() {
        WebDriver driver = mock(WebDriver.class);
        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true, new FakeActionDelay());
        assertEquals(ActionResult.SKIPPED, performer.block(null));
    }

    @Test
    void dryRunShouldOpenMenuAndLocateBlockButNeverConfirm() {

        WebDriver driver = mock(WebDriver.class);
        WebElement optionsButton = mock(WebElement.class);
        WebElement blockMenuItem = mock(WebElement.class);

        when(driver.findElement(OPTIONS_BUTTON)).thenReturn(optionsButton);
        when(driver.findElement(BLOCK_MENU_ITEM)).thenReturn(blockMenuItem);

        makeClickable(optionsButton);
        makeClickable(blockMenuItem);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true, new FakeActionDelay());

        ActionResult result = performer.block("test.user");

        assertEquals(ActionResult.WOULD_EXECUTE, result);
        verify(optionsButton, times(1)).click();
        verify(blockMenuItem, times(1)).click();
        verify(driver, never()).findElement(CONFIRM_BLOCK_BUTTON);
        verify(driver, never()).findElement(DISMISS_BUTTON);
    }

    @Test
    void executeModeShouldRunFullSequenceAndReturnExecuted() {

        WebDriver driver = mock(WebDriver.class);
        WebElement optionsButton = mock(WebElement.class);
        WebElement blockMenuItem = mock(WebElement.class);
        WebElement confirmButton = mock(WebElement.class);
        WebElement dismissButton = mock(WebElement.class);

        when(driver.findElement(OPTIONS_BUTTON)).thenReturn(optionsButton);
        when(driver.findElement(BLOCK_MENU_ITEM)).thenReturn(blockMenuItem);
        when(driver.findElement(CONFIRM_BLOCK_BUTTON)).thenReturn(confirmButton);
        when(driver.findElement(DISMISS_BUTTON)).thenReturn(dismissButton);

        makeClickable(optionsButton);
        makeClickable(blockMenuItem);
        makeClickable(confirmButton);
        makeClickable(dismissButton);

        FakeActionDelay actionDelay = new FakeActionDelay();

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, false, actionDelay);

        ActionResult result = performer.block("test.user");

        assertEquals(ActionResult.EXECUTED, result);
        verify(optionsButton, times(1)).click();
        verify(blockMenuItem, times(1)).click();
        verify(confirmButton, times(1)).click();
        verify(dismissButton, times(1)).click();
        assertEquals(1, actionDelay.getCallCount());
    }

    @Test
    void shouldReportBlockControlPresent() {
        WebDriver driver = mock(WebDriver.class);
        WebElement blockMenuItem = mock(WebElement.class);
        when(driver.findElements(BLOCK_MENU_ITEM))
            .thenReturn(Collections.singletonList(blockMenuItem));
        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true, new FakeActionDelay());
        assertTrue(performer.isBlockControlPresent());
    }

    @Test
    void shouldReportBlockControlAbsent() {
        WebDriver driver = mock(WebDriver.class);
        when(driver.findElements(BLOCK_MENU_ITEM))
            .thenReturn(Collections.emptyList());
        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true, new FakeActionDelay());
        assertFalse(performer.isBlockControlPresent());
    }
}