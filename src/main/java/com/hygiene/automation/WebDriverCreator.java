package com.hygiene.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeOptions;

@FunctionalInterface
public interface WebDriverCreator {

    WebDriver create(EdgeOptions options);
}