package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class ActionManagerTest {

    @Test
    void shouldReturnSkippedForBlankUsername() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        ActionManager manager =
            new ActionManager(
                true,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                " ",
                true
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );

        verify(
            performer,
            never()
        ).block(" ");

        verify(
            delay,
            never()
        ).waitBeforeAction(5);
    }

    @Test
    void shouldReturnSkippedWhenProfileIsNotVerified() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        ActionManager manager =
            new ActionManager(
                true,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                "user2",
                false
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );

        verify(
            performer,
            never()
        ).block("user2");

        verify(
            delay,
            never()
        ).waitBeforeAction(5);
    }

    @Test
    void shouldReturnWouldExecuteInDryRunMode() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        ActionManager manager =
            new ActionManager(
                true,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                "user2",
                true
            );

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            result
        );

        verify(
            performer,
            never()
        ).block("user2");

        verify(
            delay,
            never()
        ).waitBeforeAction(5);
    }

    @Test
    void shouldWaitAndDelegateWhenNotDryRun() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        when(
            performer.block("user2")
        ).thenReturn(
            ActionResult.SKIPPED
        );

        ActionManager manager =
            new ActionManager(
                false,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                "user2",
                true
            );

        assertEquals(
            ActionResult.SKIPPED,
            result
        );

        verify(
            delay
        ).waitBeforeAction(5);

        verify(
            performer
        ).block("user2");
    }

    @Test
    void shouldPropagateExecutedResultFromPerformer() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        when(
            performer.block("user2")
        ).thenReturn(
            ActionResult.EXECUTED
        );

        ActionManager manager =
            new ActionManager(
                false,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                "user2",
                true
            );

        assertEquals(
            ActionResult.EXECUTED,
            result
        );

        verify(
            delay
        ).waitBeforeAction(5);

        verify(
            performer
        ).block("user2");
    }

    @Test
    void shouldPropagateFailedResultFromPerformer() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        when(
            performer.block("user2")
        ).thenReturn(
            ActionResult.FAILED
        );

        ActionManager manager =
            new ActionManager(
                false,
                performer,
                delay,
                5
            );

        ActionResult result =
            manager.process(
                "user2",
                true
            );

        assertEquals(
            ActionResult.FAILED,
            result
        );

        verify(
            delay
        ).waitBeforeAction(5);

        verify(
            performer
        ).block("user2");
    }

    @Test
    void shouldRejectNullBlockActionPerformer() {

        ActionDelay delay =
            mock(ActionDelay.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new ActionManager(
                true,
                null,
                delay,
                5
            )
        );
    }

    @Test
    void shouldRejectNullActionDelay() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new ActionManager(
                true,
                performer,
                null,
                5
            )
        );
    }

    @Test
    void shouldRejectNegativeActionDelay() {

        BlockActionPerformer performer =
            mock(BlockActionPerformer.class);

        ActionDelay delay =
            mock(ActionDelay.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new ActionManager(
                true,
                performer,
                delay,
                -1
            )
        );
    }
}