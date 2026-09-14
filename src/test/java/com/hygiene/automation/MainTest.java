package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void shouldCreateDefaultMain() {

        Main main =
            new Main();

        assertNotNull(main);
    }

    @Test
    void shouldReturnZeroWithoutCallingExit() {

        Application application =
            mock(Application.class);

        ExitHandler exitHandler =
            mock(ExitHandler.class);

        when(application.run(any()))
            .thenReturn(0);

        Main main =
            new Main(
                application,
                exitHandler
            );

        String[] args = {
            "--dry-run"
        };

        int result =
            main.run(args);

        assertEquals(
            0,
            result
        );

        verify(application)
            .run(args);

        verify(exitHandler, never())
            .exit(0);
    }

    @Test
    void shouldCallExitForNonZeroExitCode() {

        Application application =
            mock(Application.class);

        ExitHandler exitHandler =
            mock(ExitHandler.class);

        when(application.run(any()))
            .thenReturn(2);

        Main main =
            new Main(
                application,
                exitHandler
            );

        String[] args = {
            "--unknown"
        };

        int result =
            main.run(args);

        assertEquals(
            2,
            result
        );

        verify(application)
            .run(args);

        verify(exitHandler)
            .exit(2);
    }

    @Test
    void shouldRejectNullApplication() {

        ExitHandler exitHandler =
            mock(ExitHandler.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new Main(
                null,
                exitHandler
            )
        );
    }

    @Test
    void shouldRejectNullExitHandler() {

        Application application =
            mock(Application.class);

        assertThrows(
            IllegalArgumentException.class,
            () -> new Main(
                application,
                null
            )
        );
    }
}