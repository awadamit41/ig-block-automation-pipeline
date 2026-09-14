package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;

class EdgeBrowserDriverFactoryTest {

    @Test
    void shouldRejectNullDriverCreator() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new EdgeBrowserDriverFactory(null)
        );
    }

    @Test
    void shouldRejectNullConfiguration() {

        WebDriverCreator creator =
            mock(WebDriverCreator.class);

        EdgeBrowserDriverFactory factory =
            new EdgeBrowserDriverFactory(creator);

        assertThrows(
            IllegalArgumentException.class,
            () -> factory.create(null)
        );
    }

    @Test
    void shouldCreateWebDriverWithConfiguredProfile()
        throws Exception {

        AppConfig config =
            mock(AppConfig.class);

        when(config.getProfileDirectory())
            .thenReturn("test-profile");

        WebDriver driver =
            mock(WebDriver.class);

        WebDriverCreator creator =
            mock(WebDriverCreator.class);

        when(creator.create(any(EdgeOptions.class)))
            .thenReturn(driver);

        EdgeBrowserDriverFactory factory =
            new EdgeBrowserDriverFactory(creator);

        WebDriver result =
            factory.create(config);

        assertEquals(
            driver,
            result
        );

        ArgumentCaptor<EdgeOptions> captor =
            ArgumentCaptor.forClass(
                EdgeOptions.class
            );

        verify(creator)
            .create(captor.capture());

        EdgeOptions options =
            captor.getValue();

        assertNotNull(options);

        String arguments =
            options.asMap()
                .toString();

        assertTrueContains(
            arguments,
            "test-profile"
        );
    }

    @Test
    void shouldReturnDriverCreatedByCreator() {

        AppConfig config =
            mock(AppConfig.class);

        when(config.getProfileDirectory())
            .thenReturn("profile");

        WebDriver expectedDriver =
            mock(WebDriver.class);

        WebDriverCreator creator =
            mock(WebDriverCreator.class);

        when(creator.create(any(EdgeOptions.class)))
            .thenReturn(expectedDriver);

        EdgeBrowserDriverFactory factory =
            new EdgeBrowserDriverFactory(creator);

        WebDriver actualDriver =
            factory.create(config);

        assertEquals(
            expectedDriver,
            actualDriver
        );
    }

    private void assertTrueContains(
        String actual,
        String expected
    ) {

        if (!actual.contains(expected)) {
            throw new AssertionError(
                "Expected '"
                + actual
                + "' to contain '"
                + expected
                + "'."
            );
        }
    }
}