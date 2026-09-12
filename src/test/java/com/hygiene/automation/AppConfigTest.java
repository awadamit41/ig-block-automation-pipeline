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
    Path tempDirectory;

    @Test
    void shouldLoadConfiguration() throws IOException {
        Path configFile =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            configFile,
            """
            browser=edge
            target.file=targets.csv
            log.file=logs/results.csv
            profile.directory=social-media-hygiene-edge-profile
            wait.timeout.seconds=15
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
            "social-media-hygiene-edge-profile",
            config.getProfileDirectory()
        );

        assertEquals(
            15,
            config.getWaitTimeoutSeconds()
        );

        assertTrue(config.isDryRun());
    }

    @Test
    void shouldReadDryRunAsFalse() throws IOException {
        Path configFile =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            configFile,
            """
            target.file=targets.csv
            log.file=logs/results.csv
            profile.directory=edge-profile
            wait.timeout.seconds=10
            dry.run=false
            """
        );

        AppConfig config =
            new AppConfig(configFile.toString());

        assertFalse(config.isDryRun());
    }

    @Test
    void shouldRejectMissingConfigurationFile() {
        Path missingFile =
            tempDirectory.resolve("missing.properties");

        assertThrows(
            IOException.class,
            () -> new AppConfig(missingFile.toString())
        );
    }

    @Test
    void shouldRejectMissingRequiredProperty()
        throws IOException {

        Path configFile =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            configFile,
            """
            target.file=targets.csv
            log.file=logs/results.csv
            """
        );

        AppConfig config =
            new AppConfig(configFile.toString());

        assertThrows(
            IllegalArgumentException.class,
            config::getProfileDirectory
        );
    }

    @Test
    void shouldRejectInvalidTimeout()
        throws IOException {

        Path configFile =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            configFile,
            """
            target.file=targets.csv
            log.file=logs/results.csv
            profile.directory=edge-profile
            wait.timeout.seconds=invalid
            dry.run=true
            """
        );

        AppConfig config =
            new AppConfig(configFile.toString());

        assertThrows(
            NumberFormatException.class,
            config::getWaitTimeoutSeconds
        );
    }
}