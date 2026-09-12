package com.hygiene.automation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ResultLogger {

    private final Path logFile;

    public ResultLogger(String filePath) throws IOException {

        this.logFile = Path.of(filePath);

        Path parent = logFile.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        if (!Files.exists(logFile)) {

            Files.writeString(
                    logFile,
                    "timestamp,username,navigation,verification,action\n",
                    StandardOpenOption.CREATE
            );
        }
    }

    public void log(
            String username,
            String navigation,
            String verification,
            ActionResult action
    ) {

        String timestamp =
                LocalDateTime.now().toString();

        String row =
                timestamp + ","
                + escape(username) + ","
                + escape(navigation) + ","
                + escape(verification) + ","
                + action + System.lineSeparator();

        try {

            Files.writeString(
                    logFile,
                    row,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {

            System.err.println(
                    "Unable to write result log."
            );

            System.err.println(
                    "Log file: "
                    + logFile.toAbsolutePath()
            );
        }
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\"", "\"\"");
    }
}