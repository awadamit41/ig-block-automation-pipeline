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

        assertTrue(
            options.hasDryRunOverride()
        );

        assertEquals(
            Boolean.TRUE,
            options.getDryRun()
        );
    }

    @Test
    void shouldEnableExecuteMode() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {"--execute"}
        );

        assertTrue(
            options.hasDryRunOverride()
        );

        assertEquals(
            Boolean.FALSE,
            options.getDryRun()
        );
    }

    @Test
    void shouldAcceptTargetFile() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {
                "--targets",
                "custom-targets.csv"
            }
        );

        assertTrue(
            options.hasTargetFileOverride()
        );

        assertEquals(
            "custom-targets.csv",
            options.getTargetFile()
        );
    }

    @Test
    void shouldTrimTargetFileValue() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {
                "--targets",
                "  custom-targets.csv  "
            }
        );

        assertEquals(
            "custom-targets.csv",
            options.getTargetFile()
        );
    }

    @Test
    void shouldAcceptNoArguments() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {}
        );

        assertNull(
            options.getTargetFile()
        );

        assertNull(
            options.getDryRun()
        );

        assertFalse(
            options.hasTargetFileOverride()
        );

        assertFalse(
            options.hasDryRunOverride()
        );
    }

    @Test
    void shouldHandleNullArguments() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(null);

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

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> options.parse(
                    new String[] {"--targets"}
                )
            );

        assertEquals(
            "Missing value for --targets.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankTargetFile() {

        CommandLineOptions options =
            new CommandLineOptions();

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> options.parse(
                    new String[] {
                        "--targets",
                        "   "
                    }
                )
            );

        assertEquals(
            "Target file path cannot be blank.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectUnknownArgument() {

        CommandLineOptions options =
            new CommandLineOptions();

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> options.parse(
                    new String[] {"--invalid"}
                )
            );

        assertEquals(
            "Unknown argument: --invalid",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankArgument() {

        CommandLineOptions options =
            new CommandLineOptions();

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> options.parse(
                    new String[] {" "}
                )
            );

        assertEquals(
            "Command-line argument cannot be blank.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectConflictingModes() {

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

    @Test
    void shouldAllowHelpOption() {

        CommandLineOptions options =
            new CommandLineOptions();

        options.parse(
            new String[] {"--help"}
        );

        assertNull(
            options.getTargetFile()
        );

        assertNull(
            options.getDryRun()
        );
    }
}