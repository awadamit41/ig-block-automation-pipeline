package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TargetProcessorTest {

    @Test
    void shouldProcessTargetSuccessfully() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("@test.account");

        assertEquals(
            "test.account",
            result.getUsername()
        );

        assertEquals(
            ProcessingStatus.SUCCESS,
            result.getStatus()
        );

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            result.getActionResult()
        );

        assertEquals(
            1,
            navigator.callCount
        );

        assertEquals(
            1,
            verifier.callCount
        );

        assertEquals(
            1,
            actionExecutor.callCount
        );

        assertEquals(
            1,
            recorder.entries.size()
        );
    }

    @Test
    void shouldStopWhenNavigationFails() {

        FakeNavigator navigator =
            new FakeNavigator(false);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.NAVIGATION_FAILED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.FAILED,
            result.getActionResult()
        );

        assertEquals(
            1,
            navigator.callCount
        );

        assertEquals(
            0,
            verifier.callCount
        );

        assertEquals(
            0,
            actionExecutor.callCount
        );

        assertEquals(
            1,
            recorder.entries.size()
        );
    }

    @Test
    void shouldStopWhenVerificationFails() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(false);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.VERIFICATION_FAILED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.SKIPPED,
            result.getActionResult()
        );

        assertEquals(
            1,
            navigator.callCount
        );

        assertEquals(
            1,
            verifier.callCount
        );

        assertEquals(
            0,
            actionExecutor.callCount
        );

        assertEquals(
            1,
            recorder.entries.size()
        );
    }

    @Test
    void shouldHandleUnexpectedNavigationException() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        navigator.throwException = true;

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.FAILED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.FAILED,
            result.getActionResult()
        );

        assertTrue(
            result.getMessage().contains(
                "RuntimeException"
            )
        );

        assertEquals(
            0,
            verifier.callCount
        );

        assertEquals(
            0,
            actionExecutor.callCount
        );
    }

    @Test
    void shouldHandleUnexpectedVerificationException() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        verifier.throwException = true;

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.FAILED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.FAILED,
            result.getActionResult()
        );

        assertTrue(
            result.getMessage().contains(
                "RuntimeException"
            )
        );

        assertEquals(
            0,
            actionExecutor.callCount
        );
    }

    @Test
    void shouldHandleUnexpectedActionException() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        actionExecutor.throwException = true;

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.FAILED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.FAILED,
            result.getActionResult()
        );

        assertTrue(
            result.getMessage().contains(
                "RuntimeException"
            )
        );
    }

    @Test
    void shouldHandleInvalidUsername() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("   ");

        assertEquals(
            ProcessingStatus.SKIPPED,
            result.getStatus()
        );

        assertEquals(
            ActionResult.SKIPPED,
            result.getActionResult()
        );

        assertEquals(
            0,
            navigator.callCount
        );

        assertEquals(
            0,
            verifier.callCount
        );

        assertEquals(
            0,
            actionExecutor.callCount
        );
    }

    @Test
    void shouldContinueWhenRecorderFails() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(
                ActionResult.WOULD_EXECUTE
            );

        FakeRecorder recorder =
            new FakeRecorder();

        recorder.throwException = true;

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        ProcessingResult result =
            processor.process("test.account");

        assertEquals(
            ProcessingStatus.SUCCESS,
            result.getStatus()
        );

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            result.getActionResult()
        );
    }

    // --------------------------------------------------
    // Test doubles
    // --------------------------------------------------

    private static class FakeNavigator
        implements Navigator {

        private final boolean result;
        private int callCount;
        private boolean throwException;

        FakeNavigator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean openProfile(String username) {

            callCount++;

            if (throwException) {
                throw new RuntimeException(
                    "Navigation failure"
                );
            }

            return result;
        }
    }

    private static class FakeVerifier
        implements Verifier {

        private final boolean result;
        private int callCount;
        private boolean throwException;

        FakeVerifier(boolean result) {
            this.result = result;
        }

        @Override
        public boolean verifyProfile(String username) {

            callCount++;

            if (throwException) {
                throw new RuntimeException(
                    "Verification failure"
                );
            }

            return result;
        }
    }

    private static class FakeActionExecutor
        implements ActionExecutor {

        private final ActionResult result;
        private int callCount;
        private boolean throwException;

        FakeActionExecutor(ActionResult result) {
            this.result = result;
        }

        @Override
        public ActionResult process(
            String username,
            boolean verified
        ) {

            callCount++;

            if (throwException) {
                throw new RuntimeException(
                    "Action failure"
                );
            }

            return result;
        }
    }

    private static class FakeRecorder
        implements ResultRecorder {

        private final List<String> entries =
            new ArrayList<>();

        private boolean throwException;

        @Override
        public void log(
            String username,
            String navigation,
            String verification,
            ActionResult action
        ) {

            if (throwException) {
                throw new RuntimeException(
                    "Recorder failure"
                );
            }

            entries.add(
                username
                + "|"
                + navigation
                + "|"
                + verification
                + "|"
                + action
            );
        }
    }
}