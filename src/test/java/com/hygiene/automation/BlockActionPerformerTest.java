package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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

        WebDriver driver =
            mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        ActionResult result =
            performer.block(" ");

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void shouldSkipNullUsername() {

        WebDriver driver =
            mock(WebDriver.class);

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

        WebDriver driver =
            mock(WebDriver.class);

        BlockActionPerformer performer =
            new BlockActionPerformer(driver);

        ActionResult result =
            performer.block("user2");

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }
}