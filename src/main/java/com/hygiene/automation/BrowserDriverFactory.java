package com.hygiene.automation;

import org.openqa.selenium.WebDriver;

public interface BrowserDriverFactory {

    WebDriver create(AppConfig config);
}