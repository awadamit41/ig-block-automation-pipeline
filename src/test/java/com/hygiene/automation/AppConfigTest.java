package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldLoadConfiguration() throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=10
                dry.run=true
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        assertEquals(
            "targets.csv",
            config.getTargetFile()
        );

        assertEquals(
            "logs/results.csv",
            config.getLogFile()
        );

        assertEquals(
            "test-profile",
            config.getProfileDirectory()
        );

        assertEquals(
            "edge",
            config.getBrowser()
        );

        assertEquals(
            10,
            config.getWaitTimeoutSeconds()
        );

        assertTrue(
            config.isDryRun()
        );
    }

    @Test
    void shouldReturnFalseWhenDryRunIsDisabled()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=10
                dry.run=false
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        assertFalse(
            config.isDryRun()
        );
    }

    @Test
    void shouldRejectMissingConfigurationFile() {

        Path missingFile =
            tempDir.resolve("missing.properties");

        assertThrows(
            IOException.class,
            () -> new AppConfig(
                missingFile.toString()
            )
        );
    }

    @Test
    void shouldRejectMissingRequiredProperty()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=10
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                config::isDryRun
            );

        assertEquals(
            "Missing configuration: dry.run",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectInvalidTimeout()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=invalid
                dry.run=true
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                config::getWaitTimeoutSeconds
            );

        assertEquals(
            "wait.timeout.seconds must be a valid integer.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectNonPositiveTimeout()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=0
                dry.run=true
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                config::getWaitTimeoutSeconds
            );

        assertEquals(
            "wait.timeout.seconds must be greater than 0.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectUnsupportedBrowser()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=chrome
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=10
                dry.run=true
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                config::getBrowser
            );

        assertEquals(
            "Unsupported browser: chrome. Supported browser: edge.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectInvalidDryRunValue()
        throws IOException {

        Path configFile =
            createConfig(
                """
                browser=edge
                target.file=targets.csv
                log.file=logs/results.csv
                profile.directory=test-profile
                wait.timeout.seconds=10
                dry.run=yes
                """
            );

        AppConfig config =
            new AppConfig(configFile.toString());

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                config::isDryRun
            );

        assertEquals(
            "dry.run must be either true or false.",
            exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankConfigurationPath() {

        IllegalArgumentException exception =
            assertThrows(
                IllegalArgumentException.class,
                () -> new AppConfig(" ")
            );

        assertEquals(
            "Configuration file path cannot be blank.",
            exception.getMessage()
        );
    }

    private Path createConfig(String content)
        throws IOException {

        Path configFile =
            tempDir.resolve(
                "test-" + System.nanoTime() + ".properties"
            );

        Files.writeString(
            configFile,
            content
        );

        return configFile;
    }
}