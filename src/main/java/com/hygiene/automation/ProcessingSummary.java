package com.hygiene.automation;

public class ProcessingSummary {

    private int total;
    private int successful;
    private int navigationFailed;
    private int verificationFailed;
    private int skipped;
    private int failed;

    public void record(ProcessingResult result) {

        if (result == null) {
            return;
        }

        total++;

        switch (result.getStatus()) {

            case SUCCESS:
                successful++;
                break;

            case NAVIGATION_FAILED:
                navigationFailed++;
                break;

            case VERIFICATION_FAILED:
                verificationFailed++;
                break;

            case SKIPPED:
                skipped++;
                break;

            case FAILED:
                failed++;
                break;
        }
    }

    public int getTotal() {
        return total;
    }

    public int getSuccessful() {
        return successful;
    }

    public int getNavigationFailed() {
        return navigationFailed;
    }

    public int getVerificationFailed() {
        return verificationFailed;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getFailed() {
        return failed;
    }

    public void printSummary(boolean dryRun) {

        System.out.println();
        System.out.println("==============================================");
        System.out.println(" PROCESSING SUMMARY");
        System.out.println("==============================================");

        System.out.println(
            "Total targets       : " + total
        );

        System.out.println(
            "Successful          : " + successful
        );

        System.out.println(
            "Navigation failed   : " + navigationFailed
        );

        System.out.println(
            "Verification failed : " + verificationFailed
        );

        System.out.println(
            "Skipped             : " + skipped
        );

        System.out.println(
            "Other failures      : " + failed
        );

        System.out.println(
            "Dry-run mode        : " + dryRun
        );

        System.out.println("==============================================");
    }
}