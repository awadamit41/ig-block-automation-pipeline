package com.hygiene.automation;

import org.openqa.selenium.WebDriver;

@FunctionalInterface
public interface WebDriverCreator<T> {

    WebDriver create(T options);
}