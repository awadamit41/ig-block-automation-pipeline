package com.hygiene.automation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {

    private final Properties properties = new Properties();

    public AppConfig(String filePath) throws IOException {

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            throw new IOException(
                    "Configuration file not found: "
                    + path.toAbsolutePath()
            );
        }

        try (InputStream input =
                     Files.newInputStream(path)) {

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

    public int getWaitTimeoutSeconds() {

        return Integer.parseInt(
                getRequired("wait.timeout.seconds")
        );
    }

    public boolean isDryRun() {

        return Boolean.parseBoolean(
                getRequired("dry.run")
        );
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