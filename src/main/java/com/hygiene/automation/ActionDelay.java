package com.hygiene.automation;

@FunctionalInterface
public interface ActionDelay {

    void waitBeforeAction(int seconds);
}