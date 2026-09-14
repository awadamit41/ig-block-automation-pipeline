package com.hygiene.automation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ResultLogger implements ResultRecorder {

    private final Path logPath;

    public ResultLogger(String filePath) throws IOException {

        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException(
                "Log file path cannot be blank."
            );
        }

        logPath = Path.of(filePath);

        Path parent =
            logPath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        if (!Files.exists(logPath)) {

            try (BufferedWriter writer =
                Files.newBufferedWriter(
                    logPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                )) {

                writer.write(
                    "timestamp,username,navigation,"
                    + "verification,action"
                );

                writer.newLine();
            }
        }
    }

    @Override
    public void log(
        String username,
        String navigation,
        String verification,
        ActionResult action
    ) {

        try (BufferedWriter writer =
            Files.newBufferedWriter(
                logPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            )) {

            writer.write(
                csv(LocalDateTime.now().toString())
            );

            writer.write(",");

            writer.write(
                csv(username)
            );

            writer.write(",");

            writer.write(
                csv(navigation)
            );

            writer.write(",");

            writer.write(
                csv(verification)
            );

            writer.write(",");

            writer.write(
                csv(
                    action == null
                        ? null
                        : action.name()
                )
            );

            writer.newLine();

        } catch (IOException e) {

            throw new IllegalStateException(
                "Unable to write result log: "
                + logPath.toAbsolutePath(),
                e
            );
        }
    }

    private String csv(String value) {

        if (value == null) {
            return "";
        }

        String escaped =
            value.replace("\"", "\"\"");

        return "\""
            + escaped
            + "\"";
    }
}