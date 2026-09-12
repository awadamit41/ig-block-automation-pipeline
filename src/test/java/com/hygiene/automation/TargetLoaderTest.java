package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TargetLoaderTest {

    @TempDir
    Path tempDirectory;

    @Test
    void shouldLoadValidUniqueUsernames() throws IOException {
        Path targetFile = tempDirectory.resolve("targets.csv");

        Files.writeString(
            targetFile,
            """
            username
            instagram
            test.account
            example_account
            """
        );

        TargetLoader loader =
            new TargetLoader(targetFile.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "instagram",
                "test.account",
                "example_account"
            ),
            targets
        );
    }

    @Test
    void shouldRemoveDuplicateUsernames() throws IOException {
        Path targetFile = tempDirectory.resolve("targets.csv");

        Files.writeString(
            targetFile,
            """
            username
            instagram
            instagram
            test.account
            instagram
            """
        );

        TargetLoader loader =
            new TargetLoader(targetFile.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "instagram",
                "test.account"
            ),
            targets
        );
    }

    @Test
    void shouldRemoveAtSymbol() throws IOException {
        Path targetFile = tempDirectory.resolve("targets.csv");

        Files.writeString(
            targetFile,
            """
            username
            @instagram
            @test.account
            """
        );

        TargetLoader loader =
            new TargetLoader(targetFile.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "instagram",
                "test.account"
            ),
            targets
        );
    }

    @Test
    void shouldIgnoreEmptyLines() throws IOException {
        Path targetFile = tempDirectory.resolve("targets.csv");

        Files.writeString(
            targetFile,
            """
            username

            instagram

            test.account

            """
        );

        TargetLoader loader =
            new TargetLoader(targetFile.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "instagram",
                "test.account"
            ),
            targets
        );
    }

    @Test
    void shouldSkipInvalidUsernames() throws IOException {
        Path targetFile = tempDirectory.resolve("targets.csv");

        Files.writeString(
            targetFile,
            """
            username
            instagram
            invalid username
            test@account
            test.account
            """
        );

        TargetLoader loader =
            new TargetLoader(targetFile.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "instagram",
                "test.account"
            ),
            targets
        );
    }

    @Test
    void shouldFailWhenTargetFileDoesNotExist() {
        Path missingFile =
            tempDirectory.resolve("missing.csv");

        TargetLoader loader =
            new TargetLoader(missingFile.toString());

        IOException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                IOException.class,
                loader::loadTargets
            );

        assertTrue(
            exception.getMessage()
                .contains("Target file not found")
        );
    }
}