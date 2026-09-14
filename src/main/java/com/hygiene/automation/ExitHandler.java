package com.hygiene.automation;

@FunctionalInterface
public interface ExitHandler {

    void exit(int exitCode);
}