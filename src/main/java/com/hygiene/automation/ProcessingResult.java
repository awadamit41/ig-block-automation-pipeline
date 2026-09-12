package com.hygiene.automation;

public class ProcessingResult {

    private final String username;
    private final ProcessingStatus status;
    private final ActionResult actionResult;
    private final String message;

    public ProcessingResult(
            String username,
            ProcessingStatus status,
            ActionResult actionResult,
            String message
    ) {
        this.username = username;
        this.status = status;
        this.actionResult = actionResult;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public ProcessingStatus getStatus() {
        return status;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public String getMessage() {
        return message;
    }
}