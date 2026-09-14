package com.hygiene.automation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ResultLoggerTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldCreateLogFileWithHeader()
        throws IOException {

        Path logFile =
            tempDir.resolve("results.csv");

        new ResultLogger(logFile.toString());

        assertTrue(
            Files.exists(logFile)
        );

        List<String> lines =
            Files.readAllLines(logFile);

        assertEquals(
            1,
            lines.size()
        );

        assertEquals(
            "timestamp,username,navigation,verification,action",
            lines.get(0)
        );
    }

    @Test
    void shouldAppendResultToLog()
        throws IOException {

        Path logFile =
            tempDir.resolve("results.csv");

        ResultLogger logger =
            new ResultLogger(
                logFile.toString()
            );

        logger.log(
            "test.account",
            "SUCCESS",
            "VERIFIED",
            ActionResult.WOULD_EXECUTE
        );

        List<String> lines =
            Files.readAllLines(logFile);

        assertEquals(
            2,
            lines.size()
        );

        assertTrue(
            lines.get(1).contains(
                "\"test.account\""
            )
        );

        assertTrue(
            lines.get(1).contains(
                "\"SUCCESS\""
            )
        );

        assertTrue(
            lines.get(1).contains(
                "\"VERIFIED\""
            )
        );

        assertTrue(
            lines.get(1).contains(
                "\"WOULD_EXECUTE\""
            )
        );
    }

    @Test
    void shouldEscapeCommasAndQuotes()
        throws IOException {

        Path logFile =
            tempDir.resolve("results.csv");

        ResultLogger logger =
            new ResultLogger(
                logFile.toString()
            );

        logger.log(
            "test,user \"account\"",
            "SUCCESS",
            "VERIFIED",
            ActionResult.WOULD_EXECUTE
        );

        List<String> lines =
            Files.readAllLines(logFile);

        assertEquals(
            2,
            lines.size()
        );

        String resultLine =
            lines.get(1);

        assertTrue(
            resultLine.contains(
                "\"test,user \"\"account\"\"\""
            )
        );
    }

    @Test
    void shouldHandleNullAction()
        throws IOException {

        Path logFile =
            tempDir.resolve("results.csv");

        ResultLogger logger =
            new ResultLogger(
                logFile.toString()
            );

        logger.log(
            "test.account",
            "SUCCESS",
            "VERIFIED",
            null
        );

        List<String> lines =
            Files.readAllLines(logFile);

        assertEquals(
            2,
            lines.size()
        );

        String resultLine =
            lines.get(1);

        assertTrue(
            resultLine.endsWith(",")
        );
    }

    @Test
    void shouldRejectBlankLogPath() {

        IllegalArgumentException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ResultLogger(" ")
            );

        assertEquals(
            "Log file path cannot be blank.",
            exception.getMessage()
        );
    }

    @Test
    void shouldCreateParentDirectories()
        throws IOException {

        Path logFile =
            tempDir
                .resolve("nested")
                .resolve("logs")
                .resolve("results.csv");

        new ResultLogger(
            logFile.toString()
        );

        assertTrue(
            Files.exists(logFile)
        );

        assertTrue(
            Files.isDirectory(
                logFile.getParent()
            )
        );
    }
}