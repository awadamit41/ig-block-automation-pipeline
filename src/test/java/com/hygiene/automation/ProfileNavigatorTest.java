package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

@ExtendWith(MockitoExtension.class)
class ProfileNavigatorTest {

    @Mock
    private WebDriver driver;

    private ProfileNavigator navigator;

    @BeforeEach
    void setUp() {
        navigator = new ProfileNavigator(driver, 1);
    }

    @Test
    void shouldOpenValidProfile() {

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
            );

        assertTrue(
            navigator.openProfile("testuser")
        );

        verify(driver).get(
            "https://www.instagram.com/testuser/"
        );
    }

    @Test
    void shouldRemoveAtSymbolFromUsername() {

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
            );

        assertTrue(
            navigator.openProfile("@testuser")
        );

        verify(driver).get(
            "https://www.instagram.com/testuser/"
        );
    }

    @Test
    void shouldHandleUppercaseUsername() {

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
            );

        assertTrue(
            navigator.openProfile("TestUser")
        );

        verify(driver).get(
            "https://www.instagram.com/TestUser/"
        );
    }

    @Test
    void shouldAcceptUsernameWithDotAndUnderscore() {

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/test.user_01/"
            );

        assertTrue(
            navigator.openProfile("test.user_01")
        );
    }

    @Test
    void shouldIgnoreQueryParametersAndFragments() {

        when(driver.getCurrentUrl())
            .thenReturn(
                "https://www.instagram.com/testuser/"
                + "?hl=en#profile"
            );

        assertTrue(
            navigator.openProfile("testuser")
        );
    }

    @Test
    void shouldRejectNullUsername() {

        assertFalse(
            navigator.openProfile(null)
        );
    }

    @Test
    void shouldRejectBlankUsername() {

        assertFalse(
            navigator.openProfile("   ")
        );
    }

    @Test
    void shouldRejectInvalidUsername() {

        assertFalse(
            navigator.openProfile("test-user")
        );
    }

    @Test
    void shouldRejectUsernameWithSpaces() {

        assertFalse(
            navigator.openProfile("test user")
        );
    }

    @Test
    void shouldRejectOnlyAtSymbol() {

        assertFalse(
            navigator.openProfile("@")
        );
    }

    @Test
    void shouldReturnFalseWhenNavigationThrowsException() {

        doThrow(
            new RuntimeException("Browser navigation failed")
        )
        .when(driver)
        .get(
            "https://www.instagram.com/testuser/"
        );

        assertFalse(
            navigator.openProfile("testuser")
        );
    }
}