package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ActionManagerTest {

    @Test
    void dryRunShouldReturnWouldExecuteForVerifiedProfile() {

        ActionManager actionManager =
            new ActionManager(true);

        ActionResult result =
            actionManager.process(
                "test.account",
                true
            );

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            result
        );
    }

    @Test
    void unverifiedProfileShouldBeSkipped() {

        ActionManager actionManager =
            new ActionManager(true);

        ActionResult result =
            actionManager.process(
                "test.account",
                false
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void blankUsernameShouldBeSkipped() {

        ActionManager actionManager =
            new ActionManager(true);

        ActionResult result =
            actionManager.process(
                "",
                true
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void nullUsernameShouldBeSkipped() {

        ActionManager actionManager =
            new ActionManager(true);

        ActionResult result =
            actionManager.process(
                null,
                true
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }

    @Test
    void disabledExecutionShouldSkipAction() {

        ActionManager actionManager =
            new ActionManager(false);

        ActionResult result =
            actionManager.process(
                "test.account",
                true
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );
    }
}