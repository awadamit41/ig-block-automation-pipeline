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

    private static final By BLOCK_BUTTON =
        By.xpath("//button[normalize-space()='Block']");

    @Test
    void shouldRejectNullDriver() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new BlockActionPerformer(null, true)
        );
    }

    @Test
    void shouldSkipBlankUsername() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        ActionResult result =
            performer.block("");

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void shouldSkipNullUsername() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        ActionResult result =
            performer.block(null);

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void dryRunShouldLocateButtonButNeverClickIt() {

        WebDriver driver = mock(WebDriver.class);
        WebElement blockButton = mock(WebElement.class);

        when(driver.findElement(BLOCK_BUTTON))
            .thenReturn(blockButton);
        when(blockButton.isDisplayed()).thenReturn(true);
        when(blockButton.isEnabled()).thenReturn(true);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        ActionResult result =
            performer.block("test.user");

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            result
        );

        verify(blockButton, never()).click();
    }

    @Test
    void executeModeShouldClickTheButtonOnce() {

        WebDriver driver = mock(WebDriver.class);
        WebElement blockButton = mock(WebElement.class);

        when(driver.findElement(BLOCK_BUTTON))
            .thenReturn(blockButton);
        when(blockButton.isDisplayed()).thenReturn(true);
        when(blockButton.isEnabled()).thenReturn(true);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, false);

        ActionResult result =
            performer.block("test.user");

        assertEquals(
            ActionResult.EXECUTED,
            result
        );

        verify(blockButton, times(1)).click();
    }

    @Test
    void shouldReportBlockControlPresent() {

        WebDriver driver = mock(WebDriver.class);
        WebElement blockButton = mock(WebElement.class);

        when(driver.findElements(BLOCK_BUTTON))
            .thenReturn(Collections.singletonList(blockButton));

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        assertTrue(
            performer.isBlockControlPresent()
        );
    }

    @Test
    void shouldReportBlockControlAbsent() {

        WebDriver driver = mock(WebDriver.class);

        when(driver.findElements(BLOCK_BUTTON))
            .thenReturn(Collections.emptyList());

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        assertFalse(
            performer.isBlockControlPresent()
        );
    }

    @Test
    void shouldRejectZeroTimeout() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        assertThrows(
            IllegalArgumentException.class,
            () -> performer.findBlockControl(0)
        );
    }

    @Test
    void shouldRejectNegativeTimeout() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver, true);

        assertThrows(
            IllegalArgumentException.class,
            () -> performer.findBlockControl(-1)
        );
    }
}