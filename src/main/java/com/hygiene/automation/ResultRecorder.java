package com.hygiene.automation;

public interface ResultRecorder {

    void log(
        String username,
        String navigation,
        String verification,
        ActionResult action
    );
}