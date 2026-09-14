package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.WebDriver;

class ApplicationTest {

    @TempDir
    Path tempDirectory;

    @Test
    void shouldRejectNullBrowserDriverFactory() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new Application(null)
        );
    }

    @Test
    void shouldRejectBlankConfigurationPath() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new Application(factory, "   ")
        );
    }

    @Test
    void shouldRejectNullConfigurationPath() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new Application(factory, null)
        );
    }

    @Test
    void shouldReturnTwoForUnknownArgument() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        Application application =
            new Application(factory);

        assertEquals(
            2,
            application.run(
                new String[] {
                    "--unknown"
                }
            )
        );

        verify(factory, never())
            .create(any(AppConfig.class));
    }

    @Test
    void shouldReturnTwoForMissingTargetArgument() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        Application application =
            new Application(factory);

        assertEquals(
            2,
            application.run(
                new String[] {
                    "--targets"
                }
            )
        );

        verify(factory, never())
            .create(any(AppConfig.class));
    }

    @Test
    void shouldReturnTwoForConflictingModes() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        Application application =
            new Application(factory);

        assertEquals(
            2,
            application.run(
                new String[] {
                    "--dry-run",
                    "--execute"
                }
            )
        );

        verify(factory, never())
            .create(any(AppConfig.class));
    }

    @Test
    void shouldReturnOneWhenConfigurationDoesNotExist() {

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        Application application =
            new Application(
                factory,
                tempDirectory
                    .resolve("missing.properties")
                    .toString()
            );

        assertEquals(
            1,
            application.run(new String[0])
        );

        verify(factory, never())
            .create(any(AppConfig.class));
    }

    @Test
    void shouldProcessTargetsWithInjectedBrowserFactory()
        throws Exception {

        Path targetsPath =
            tempDirectory.resolve("targets.csv");

        Path logPath =
            tempDirectory.resolve("results.csv");

        Path configPath =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            targetsPath,
            "username\n"
            + "testuser\n"
        );

        writeConfig(
            configPath,
            targetsPath,
            logPath
        );

        WebDriver driver =
            mock(WebDriver.class);

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
            );

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        when(factory.create(any(AppConfig.class)))
            .thenReturn(driver);

        Application application =
            new Application(
                factory,
                configPath.toString()
            );

        int exitCode =
            application.run(new String[0]);

        assertEquals(
            0,
            exitCode
        );

        verify(factory)
            .create(any(AppConfig.class));

        verify(driver)
            .quit();

        String log =
            Files.readString(logPath);

        assertTrue(
            log.contains("testuser")
        );
    }

    @Test
    void shouldUseTargetFileCommandLineOverride()
        throws Exception {

        Path configuredTargets =
            tempDirectory.resolve(
                "configured.csv"
            );

        Path overrideTargets =
            tempDirectory.resolve(
                "override.csv"
            );

        Path logPath =
            tempDirectory.resolve("results.csv");

        Path configPath =
            tempDirectory.resolve(
                "config.properties"
            );

        Files.writeString(
            configuredTargets,
            "username\n"
            + "configureduser\n"
        );

        Files.writeString(
            overrideTargets,
            "username\n"
            + "overrideuser\n"
        );

        writeConfig(
            configPath,
            configuredTargets,
            logPath
        );

        WebDriver driver =
            mock(WebDriver.class);

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/overrideuser/"
            );

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        when(factory.create(any(AppConfig.class)))
            .thenReturn(driver);

        Application application =
            new Application(
                factory,
                configPath.toString()
            );

        int exitCode =
            application.run(
                new String[] {
                    "--targets",
                    overrideTargets.toString()
                }
            );

        assertEquals(
            0,
            exitCode
        );

        String log =
            Files.readString(logPath);

        assertTrue(
            log.contains("overrideuser")
        );
    }

    @Test
    void shouldContinueWhenTargetsAreProcessed()
        throws Exception {

        Path targetsPath =
            tempDirectory.resolve("targets.csv");

        Path logPath =
            tempDirectory.resolve("results.csv");

        Path configPath =
            tempDirectory.resolve("config.properties");

        Files.writeString(
            targetsPath,
            "username\n"
            + "firstuser\n"
            + "seconduser\n"
        );

        writeConfig(
            configPath,
            targetsPath,
            logPath
        );

        WebDriver driver =
            mock(WebDriver.class);

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/firstuser/",
                "https://www.instagram.com/seconduser/"
            );

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        when(factory.create(any(AppConfig.class)))
            .thenReturn(driver);

        Application application =
            new Application(
                factory,
                configPath.toString()
            );

        int exitCode =
            application.run(new String[0]);

        assertEquals(
            0,
            exitCode
        );

        verify(driver)
            .quit();
    }

    @Test
    void shouldHandleBrowserStartupFailure()
        throws Exception {

        Path configPath =
            tempDirectory.resolve(
                "config.properties"
            );

        Path targetsPath =
            tempDirectory.resolve(
                "targets.csv"
            );

        Path logPath =
            tempDirectory.resolve(
                "results.csv"
            );

        Files.writeString(
            targetsPath,
            "username\n"
            + "testuser\n"
        );

        writeConfig(
            configPath,
            targetsPath,
            logPath
        );

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        when(factory.create(any(AppConfig.class)))
            .thenThrow(
                new RuntimeException(
                    "Browser startup failed"
                )
            );

        Application application =
            new Application(
                factory,
                configPath.toString()
            );

        assertEquals(
            1,
            application.run(new String[0])
        );
    }

    @Test
    void shouldHandleBrowserQuitFailure()
        throws Exception {

        Path configPath =
            tempDirectory.resolve(
                "config.properties"
            );

        Path targetsPath =
            tempDirectory.resolve(
                "targets.csv"
            );

        Path logPath =
            tempDirectory.resolve(
                "results.csv"
            );

        Files.writeString(
            targetsPath,
            "username\n"
            + "testuser\n"
        );

        writeConfig(
            configPath,
            targetsPath,
            logPath
        );

        WebDriver driver =
            mock(WebDriver.class);

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
            );

        doThrow(
            new RuntimeException(
                "Unable to close browser"
            )
        )
        .when(driver)
        .quit();

        BrowserDriverFactory factory =
            mock(BrowserDriverFactory.class);

        when(factory.create(any(AppConfig.class)))
            .thenReturn(driver);

        Application application =
            new Application(
                factory,
                configPath.toString()
            );

        assertEquals(
            0,
            application.run(new String[0])
        );

        verify(driver)
            .quit();
    }

    private void writeConfig(
        Path configPath,
        Path targetsPath,
        Path logPath
    ) throws Exception {

        String targets =
            targetsPath.toAbsolutePath()
                .toString()
                .replace("\\", "/");

        String log =
            logPath.toAbsolutePath()
                .toString()
                .replace("\\", "/");

        Files.writeString(
            configPath,
            "browser=edge\n"
            + "target.file=" + targets + "\n"
            + "log.file=" + log + "\n"
            + "profile.directory=test-profile\n"
            + "wait.timeout.seconds=1\n"
            + "dry.run=true\n"
        );
    }
}