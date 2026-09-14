package com.hygiene.automation;

public class Main {

    public static void main(String[] args) {

        System.out.println(
            "=============================================="
        );

        System.out.println(
            " Social Media Hygiene Automation"
        );

        System.out.println(
            "=============================================="
        );

        Application application =
            new Application();

        int exitCode =
            application.run(args);

        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}