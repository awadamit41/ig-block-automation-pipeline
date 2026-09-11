package com.hygiene.automation;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TargetLoader {

    private final Path csvPath;

    public TargetLoader(String filePath) {
        this.csvPath = Path.of(filePath);
    }

    public List<String> loadTargets() throws IOException {

        if (!Files.exists(csvPath)) {
            throw new IOException(
                    "Target file not found: " + csvPath.toAbsolutePath()
            );
        }

        Set<String> uniqueTargets = new LinkedHashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // Skip CSV header
                if (firstLine && line.equalsIgnoreCase("username")) {
                    firstLine = false;
                    continue;
                }

                firstLine = false;

                // Remove optional @
                if (line.startsWith("@")) {
                    line = line.substring(1);
                }

                // Validate Instagram-style username
                if (!line.matches("[A-Za-z0-9._]+")) {
                    System.out.println(
                            "Skipping invalid username: " + line
                    );
                    continue;
                }

                uniqueTargets.add(line);
            }
        }

        return new ArrayList<>(uniqueTargets);
    }
}