package com.hygiene.automation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TargetLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldLoadValidUniqueUsernames()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username
                user.one
                user_two
                thirduser
                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "user.one",
                "user_two",
                "thirduser"
            ),
            targets
        );
    }

    @Test
    void shouldRemoveDuplicateUsernames()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username
                test.account
                test.account
                another.account
                test.account
                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "test.account",
                "another.account"
            ),
            targets
        );
    }

    @Test
    void shouldRemoveLeadingAtSymbol()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username
                @test.account
                @another_user
                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "test.account",
                "another_user"
            ),
            targets
        );
    }

    @Test
    void shouldIgnoreEmptyLines()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username

                test.account


                another.account

                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "test.account",
                "another.account"
            ),
            targets
        );
    }

    @Test
    void shouldIgnoreWhitespaceAroundUsernames()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username
                  test.account
                @another_user
                  third.user
                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "test.account",
                "another_user",
                "third.user"
            ),
            targets
        );
    }

    @Test
    void shouldSkipInvalidUsernames()
        throws IOException {

        Path file =
            createTargetFile(
                """
                username
                valid.account
                invalid username
                test@account
                another-valid
                valid_user
                """
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertEquals(
            List.of(
                "valid.account",
                "valid_user"
            ),
            targets
        );
    }

    @Test
    void shouldRejectMissingTargetFile() {

        Path missingFile =
            tempDir.resolve(
                "missing.csv"
            );

        TargetLoader loader =
            new TargetLoader(
                missingFile.toString()
            );

        IOException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                IOException.class,
                loader::loadTargets
            );

        assertTrue(
            exception.getMessage().contains(
                "Target file not found:"
            )
        );
    }

    @Test
    void shouldReturnEmptyListForEmptyFile()
        throws IOException {

        Path file =
            createTargetFile("");

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertTrue(
            targets.isEmpty()
        );
    }

    @Test
    void shouldReturnEmptyListForHeaderOnlyFile()
        throws IOException {

        Path file =
            createTargetFile(
                "username"
            );

        TargetLoader loader =
            new TargetLoader(file.toString());

        List<String> targets =
            loader.loadTargets();

        assertTrue(
            targets.isEmpty()
        );
    }

    private Path createTargetFile(
        String content
    ) throws IOException {

        Path file =
            tempDir.resolve(
                "targets-"
                + System.nanoTime()
                + ".csv"
            );

        Files.writeString(
            file,
            content
        );

        return file;
    }
}