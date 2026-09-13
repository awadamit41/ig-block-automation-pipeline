package com.hygiene.automation;

public class CommandLineOptions {

    private String targetFile;
    private Boolean dryRun;

    public void parse(String[] args) {

        if (args == null) {
            return;
        }

        boolean dryRunSpecified = false;
        boolean executeSpecified = false;

        for (int i = 0; i < args.length; i++) {

            String argument = args[i];

            switch (argument) {

                case "--targets":

                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException(
                            "Missing value for --targets."
                        );
                    }

                    targetFile = args[++i];
                    break;

                case "--dry-run":

                    dryRunSpecified = true;
                    dryRun = true;
                    break;

                case "--execute":

                    executeSpecified = true;
                    dryRun = false;
                    break;

                case "--help":

                    printHelp();
                    break;

                default:

                    throw new IllegalArgumentException(
                        "Unknown argument: " + argument
                    );
            }
        }

        if (dryRunSpecified && executeSpecified) {
            throw new IllegalArgumentException(
                "--dry-run and --execute cannot be used together."
            );
        }
    }

    public String getTargetFile() {
        return targetFile;
    }

    public Boolean getDryRun() {
        return dryRun;
    }

    public boolean hasTargetFileOverride() {
        return targetFile != null;
    }

    public boolean hasDryRunOverride() {
        return dryRun != null;
    }

    private void printHelp() {

        System.out.println();
        System.out.println("Social Media Hygiene Automation");
        System.out.println();
        System.out.println("Options:");
        System.out.println(
            "  --targets <file>  Use a different target CSV file."
        );
        System.out.println(
            "  --dry-run         Run without account-changing actions."
        );
        System.out.println(
            "  --execute         Enable configured execution mode."
        );
        System.out.println(
            "  --help            Show this help message."
        );
        System.out.println();
    }
}