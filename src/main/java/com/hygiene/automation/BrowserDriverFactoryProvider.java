package com.hygiene.automation;

public final class BrowserDriverFactoryProvider {

    private BrowserDriverFactoryProvider() {
    }

    public static BrowserDriverFactory forBrowser(
        String browser
    ) {

        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException(
                "Browser cannot be blank."
            );
        }

        return switch (browser.trim().toLowerCase()) {
            case "chrome" ->
                new ChromeBrowserDriverFactory();

            case "firefox" ->
                new FirefoxBrowserDriverFactory();

            case "edge" ->
                new EdgeBrowserDriverFactory();

            default ->
                throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
                );
        };
    }
}