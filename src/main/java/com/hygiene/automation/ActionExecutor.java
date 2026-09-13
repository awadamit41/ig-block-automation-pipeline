package com.hygiene.automation;

public interface ActionExecutor {

    ActionResult process(String username, boolean verified);
}