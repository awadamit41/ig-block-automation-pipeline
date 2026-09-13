package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TargetProcessorTest {

    @Test
    void shouldSuccessfullyProcessVerifiedTarget() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(ActionResult.WOULD_EXECUTE);

        FakeResultRecorder recorder =
            new FakeResultRecorder();

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

        assertTrue(navigator.called);
        assertTrue(verifier.called);
        assertTrue(actionExecutor.called);
        assertTrue(recorder.called);
    }

    @Test
    void shouldStopWhenNavigationFails() {

        FakeNavigator navigator =
            new FakeNavigator(false);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(ActionResult.WOULD_EXECUTE);

        FakeResultRecorder recorder =
            new FakeResultRecorder();

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

        assertTrue(navigator.called);
        assertFalse(verifier.called);
        assertFalse(actionExecutor.called);
        assertTrue(recorder.called);
    }

    @Test
    void shouldStopWhenVerificationFails() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(false);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(ActionResult.WOULD_EXECUTE);

        FakeResultRecorder recorder =
            new FakeResultRecorder();

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

        assertTrue(navigator.called);
        assertTrue(verifier.called);
        assertFalse(actionExecutor.called);
        assertTrue(recorder.called);
    }

    @Test
    void shouldRecordSuccessfulProcessing() {

        FakeNavigator navigator =
            new FakeNavigator(true);

        FakeVerifier verifier =
            new FakeVerifier(true);

        FakeActionExecutor actionExecutor =
            new FakeActionExecutor(ActionResult.WOULD_EXECUTE);

        FakeResultRecorder recorder =
            new FakeResultRecorder();

        TargetProcessor processor =
            new TargetProcessor(
                navigator,
                verifier,
                actionExecutor,
                recorder
            );

        processor.process("test.account");

        assertEquals(
            "test.account",
            recorder.username
        );

        assertEquals(
            "SUCCESS",
            recorder.navigation
        );

        assertEquals(
            "VERIFIED",
            recorder.verification
        );

        assertEquals(
            ActionResult.WOULD_EXECUTE,
            recorder.action
        );
    }

    private static class FakeNavigator
        implements Navigator {

        private final boolean result;
        private boolean called;

        FakeNavigator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean openProfile(String username) {
            called = true;
            return result;
        }
    }

    private static class FakeVerifier
        implements Verifier {

        private final boolean result;
        private boolean called;

        FakeVerifier(boolean result) {
            this.result = result;
        }

        @Override
        public boolean verifyProfile(String username) {
            called = true;
            return result;
        }
    }

    private static class FakeActionExecutor
        implements ActionExecutor {

        private final ActionResult result;
        private boolean called;

        FakeActionExecutor(ActionResult result) {
            this.result = result;
        }

        @Override
        public ActionResult process(
            String username,
            boolean verified
        ) {
            called = true;
            return result;
        }
    }

    private static class FakeResultRecorder
        implements ResultRecorder {

        private boolean called;
        private String username;
        private String navigation;
        private String verification;
        private ActionResult action;

        @Override
        public void log(
            String username,
            String navigation,
            String verification,
            ActionResult action
        ) {
            called = true;
            this.username = username;
            this.navigation = navigation;
            this.verification = verification;
            this.action = action;
        }
    }
}