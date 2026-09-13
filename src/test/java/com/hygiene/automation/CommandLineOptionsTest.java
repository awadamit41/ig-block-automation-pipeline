package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommandLineOptionsTest {

    @Test
    void shouldEnableDryRun() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {"--dry-run"}
        );

        assertTrue(options.hasDryRunOverride());
        assertTrue(options.getDryRun());
    }

    @Test
    void shouldDisableDryRunWithExecute() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {"--execute"}
        );

        assertTrue(options.hasDryRunOverride());
        assertFalse(options.getDryRun());
    }

    @Test
    void shouldReadTargetFile() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {
                "--targets",
                "custom-targets.csv"
            }
        );

        assertTrue(options.hasTargetFileOverride());

        assertEquals(
            "custom-targets.csv",
            options.getTargetFile()
        );
    }

    @Test
    void shouldAllowNoArguments() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(new String[] {});

        assertFalse(
            options.hasTargetFileOverride()
        );

        assertFalse(
            options.hasDryRunOverride()
        );

        assertNull(
            options.getTargetFile()
        );

        assertNull(
            options.getDryRun()
        );
    }

    @Test
    void shouldRejectMissingTargetFile() {

        CommandLineOptions options =
            new CommandLineOptions();

        assertThrows(
            IllegalArgumentException.class,
            () -> options.parse(
                new String[] {"--targets"}
            )
        );
    }

    @Test
    void shouldRejectUnknownArgument() {

        CommandLineOptions options =
            new CommandLineOptions();

        assertThrows(
            IllegalArgumentException.class,
            () -> options.parse(
                new String[] {"--unknown"}
            )
        );
    }

    @Test
    void shouldRejectConflictingExecutionModes() {

        CommandLineOptions options =
            new CommandLineOptions();

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> options.parse(
                    new String[] {
                        "--dry-run",
                        "--execute"
                    }
                )
            );

        assertEquals(
            "--dry-run and --execute cannot be used together.",
            exception.getMessage()
        );
    }
}