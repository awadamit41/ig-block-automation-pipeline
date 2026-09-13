package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProcessingSummaryTest {

    @Test
    void shouldCountSuccessfulResult() {

        ProcessingSummary summary =
            new ProcessingSummary();

        ProcessingResult result =
            new ProcessingResult(
                "test.account",
                ProcessingStatus.SUCCESS,
                ActionResult.WOULD_EXECUTE,
                "Target processed successfully."
            );

        summary.record(result);

        assertEquals(1, summary.getTotal());
        assertEquals(1, summary.getSuccessful());
        assertEquals(0, summary.getNavigationFailed());
        assertEquals(0, summary.getVerificationFailed());
        assertEquals(0, summary.getSkipped());
        assertEquals(0, summary.getFailed());
    }

    @Test
    void shouldCountDifferentStatuses() {

        ProcessingSummary summary =
            new ProcessingSummary();

        summary.record(
            new ProcessingResult(
                "one",
                ProcessingStatus.SUCCESS,
                ActionResult.WOULD_EXECUTE,
                "success"
            )
        );

        summary.record(
            new ProcessingResult(
                "two",
                ProcessingStatus.NAVIGATION_FAILED,
                ActionResult.FAILED,
                "navigation failed"
            )
        );

        summary.record(
            new ProcessingResult(
                "three",
                ProcessingStatus.VERIFICATION_FAILED,
                ActionResult.SKIPPED,
                "verification failed"
            )
        );

        summary.record(
            new ProcessingResult(
                "four",
                ProcessingStatus.SKIPPED,
                ActionResult.SKIPPED,
                "skipped"
            )
        );

        summary.record(
            new ProcessingResult(
                "five",
                ProcessingStatus.FAILED,
                ActionResult.FAILED,
                "failed"
            )
        );

        assertEquals(5, summary.getTotal());
        assertEquals(1, summary.getSuccessful());
        assertEquals(1, summary.getNavigationFailed());
        assertEquals(1, summary.getVerificationFailed());
        assertEquals(1, summary.getSkipped());
        assertEquals(1, summary.getFailed());
    }

    @Test
    void shouldIgnoreNullResult() {

        ProcessingSummary summary =
            new ProcessingSummary();

        summary.record(null);

        assertEquals(0, summary.getTotal());
    }
}