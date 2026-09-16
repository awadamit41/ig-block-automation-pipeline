package com.hygiene.automation;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class BlockActionPerformerTest {

    @Test
    void shouldRejectNullDriver() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new BlockActionPerformer(null)
        );
    }

    @Test
    void shouldSkipBlankUsername() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

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
            new BlockActionPerformer(driver);

        ActionResult result =
            performer.block(null);

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void shouldKeepRealAccountChangingExecutionDisabled() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        ActionResult result =
            performer.block("user2");

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void shouldReportBlockControlPresent() {

        WebDriver driver = mock(WebDriver.class);
        WebElement blockButton =
            mock(WebElement.class);

        when(
            driver.findElements(
                org.openqa.selenium.By.xpath(
                    "//button[normalize-space()='Block']"
                )
            )
        ).thenReturn(
            Collections.singletonList(blockButton)
        );

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        assertTrue(
            performer.isBlockControlPresent()
        );
    }

    @Test
    void shouldReportBlockControlAbsent() {

        WebDriver driver = mock(WebDriver.class);

        when(
            driver.findElements(
                org.openqa.selenium.By.xpath(
                    "//button[normalize-space()='Block']"
                )
            )
        ).thenReturn(
            Collections.emptyList()
        );

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        assertFalse(
            performer.isBlockControlPresent()
        );
    }

    @Test
    void shouldRejectZeroTimeout() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        assertThrows(
            IllegalArgumentException.class,
            () -> performer.findBlockControl(0)
        );
    }

    @Test
    void shouldRejectNegativeTimeout() {

        WebDriver driver = mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        assertThrows(
            IllegalArgumentException.class,
            () -> performer.findBlockControl(-1)
        );
    }
}