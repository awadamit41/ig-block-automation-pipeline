package com.hygiene.automation;

public class Main {

    private final Application application;
    private final ExitHandler exitHandler;

    public Main() {
        this(
            new Application(),
            System::exit
        );
    }

    Main(
        Application application,
        ExitHandler exitHandler
    ) {
        if (application == null) {
            throw new IllegalArgumentException(
                "Application cannot be null."
            );
        }

        if (exitHandler == null) {
            throw new IllegalArgumentException(
                "Exit handler cannot be null."
            );
        }

        this.application = application;
        this.exitHandler = exitHandler;
    }

    public static void main(String[] args) {

        Main main =
            new Main();

        main.run(args);
    }

    int run(String[] args) {

        int exitCode =
            application.run(args);

        if (exitCode != 0) {
            exitHandler.exit(exitCode);
        }

        return exitCode;
    }
}