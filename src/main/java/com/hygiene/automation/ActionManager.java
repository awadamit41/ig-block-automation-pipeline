package com.hygiene.automation;

public class ActionManager implements ActionExecutor{

    private final boolean dryRun;

    public ActionManager(boolean dryRun) {
        this.dryRun = dryRun;
    }

    @Override
    public ActionResult process(String username, boolean verified) {

        if (username == null || username.isBlank()) {
            return ActionResult.SKIPPED;
        }

        if (!verified) {

            System.out.println(
                    "ACTION SKIPPED: profile was not verified."
            );

            return ActionResult.SKIPPED;
        }

        if (dryRun) {

            System.out.println();
            System.out.println(
                    "DRY RUN: @" + username
            );

            System.out.println(
                    "No account-changing action was performed."
            );

            return ActionResult.WOULD_EXECUTE;
        }

        /*
         * Actual account-changing operations are intentionally
         * not implemented in this prototype.
         */
        System.out.println(
                "ACTION MODE: execution is disabled."
        );

        return ActionResult.SKIPPED;
    }
}