package com.hygiene.automation;

public class ActionManager implements ActionExecutor {

    private final boolean dryRun;
    private final BlockActionPerformer blockActionPerformer;
    private final ActionDelay actionDelay;
    private final int actionDelaySeconds;

    public ActionManager(
        boolean dryRun,
        BlockActionPerformer blockActionPerformer,
        ActionDelay actionDelay,
        int actionDelaySeconds
    ) {

        if (blockActionPerformer == null) {
            throw new IllegalArgumentException(
                "Block action performer cannot be null."
            );
        }

        if (actionDelay == null) {
            throw new IllegalArgumentException(
                "Action delay cannot be null."
            );
        }

        if (actionDelaySeconds < 0) {
            throw new IllegalArgumentException(
                "Action delay seconds cannot be negative."
            );
        }

        this.dryRun = dryRun;
        this.blockActionPerformer = blockActionPerformer;
        this.actionDelay = actionDelay;
        this.actionDelaySeconds = actionDelaySeconds;
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
            System.out.println("DRY RUN: WOULD BLOCK @" + username);
            System.out.println(
                "No account-changing action was performed."
            );
            return ActionResult.WOULD_EXECUTE;
        }

        actionDelay.waitBeforeAction(actionDelaySeconds);

        return blockActionPerformer.block(username);
    }
}