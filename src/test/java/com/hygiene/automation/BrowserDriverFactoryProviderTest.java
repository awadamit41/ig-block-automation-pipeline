package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class BrowserDriverFactoryProviderTest {

    @Test
    void shouldReturnChromeFactory() {

        BrowserDriverFactory factory =
            BrowserDriverFactoryProvider
                .forBrowser("chrome");

        assertInstanceOf(
            ChromeBrowserDriverFactory.class,
            factory
        );
    }

    @Test
    void shouldReturnFirefoxFactory() {

        BrowserDriverFactory factory =
            BrowserDriverFactoryProvider
                .forBrowser("firefox");

        assertInstanceOf(
            FirefoxBrowserDriverFactory.class,
            factory
        );
    }

    @Test
    void shouldReturnEdgeFactory() {

        BrowserDriverFactory factory =
            BrowserDriverFactoryProvider
                .forBrowser("edge");

        assertInstanceOf(
            EdgeBrowserDriverFactory.class,
            factory
        );
    }

    @Test
    void shouldRejectBlankBrowser() {

        assertThrows(
            IllegalArgumentException.class,
            () ->
                BrowserDriverFactoryProvider
                    .forBrowser("")
        );
    }

    @Test
    void shouldRejectUnsupportedBrowser() {

        assertThrows(
            IllegalArgumentException.class,
            () ->
                BrowserDriverFactoryProvider
                    .forBrowser("opera")
        );
    }
}