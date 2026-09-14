package com.hygiene.automation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException(
                "Configuration file path cannot be blank."
            );
        }

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            throw new IOException(
                "Configuration file not found: "
                + path.toAbsolutePath()
            );
        }

        if (!Files.isRegularFile(path)) {
            throw new IOException(
                "Configuration path is not a file: "
                + path.toAbsolutePath()
            );
        }

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        }
    }

    public String getTargetFile() {
        return getRequired("target.file");
    }

    public String getLogFile() {
        return getRequired("log.file");
    }

    public String getProfileDirectory() {
        return getRequired("profile.directory");
    }

    public String getBrowser() {
        String browser = getRequired("browser").toLowerCase();

        if (!browser.equals("edge")) {
            throw new IllegalArgumentException(
                "Unsupported browser: " + browser
                + ". Supported browser: edge."
            );
        }

        return browser;
    }

    public int getWaitTimeoutSeconds() {
        String value = getRequired("wait.timeout.seconds");

        try {
            int timeout = Integer.parseInt(value);

            if (timeout <= 0) {
                throw new IllegalArgumentException(
                    "wait.timeout.seconds must be greater than 0."
                );
            }

            return timeout;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "wait.timeout.seconds must be a valid integer."
            );
        }
    }

    public boolean isDryRun() {
        String value = getRequired("dry.run").toLowerCase();

        if (!value.equals("true") && !value.equals("false")) {
            throw new IllegalArgumentException(
                "dry.run must be either true or false."
            );
        }

        return Boolean.parseBoolean(value);
    }

    private String getRequired(String key) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                "Missing configuration: " + key
            );
        }

        return value.trim();
    }
}