package com.hygiene.automation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(MockitoExtension.class)
class ProfileVerifierTest {

    @Mock
    private WebDriver driver;

    private ProfileVerifier verifier;

    @BeforeEach
    void setUp() {
        verifier = new ProfileVerifier(driver);
    }

    @Test
    void shouldVerifyMatchingProfileUrl() {

        when(driver.getCurrentUrl())
            .thenReturn("https://www.instagram.com/testuser/");

        assertTrue(
            verifier.verifyProfile("testuser")
        );
    }

    @Test
    void shouldVerifyUsernameWithAtSymbol() {

        when(driver.getCurrentUrl())
            .thenReturn("https://www.instagram.com/testuser/");

        assertTrue(
            verifier.verifyProfile("@testuser")
        );
    }

    @Test
    void shouldVerifyUrlCaseInsensitively() {

        when(driver.getCurrentUrl())
            .thenReturn("https://www.instagram.com/TestUser/");

        assertTrue(
            verifier.verifyProfile("testuser")
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
            verifier.verifyProfile("testuser")
        );
    }

    @Test
    void shouldRejectDifferentProfile() {

        when(driver.getCurrentUrl())
            .thenReturn("https://www.instagram.com/otheruser/");

        assertFalse(
            verifier.verifyProfile("testuser")
        );
    }

    @Test
    void shouldRejectPartialUsernameMatch() {

        when(driver.getCurrentUrl())
            .thenReturn("https://www.instagram.com/testuser123/");

        assertFalse(
            verifier.verifyProfile("testuser")
        );
    }

    @Test
    void shouldRejectBlankUsername() {

        assertFalse(
            verifier.verifyProfile("")
        );
    }

    @Test
    void shouldRejectNullUsername() {

        assertFalse(
            verifier.verifyProfile(null)
        );
    }

    @Test
    void shouldRejectNullCurrentUrl() {

        when(driver.getCurrentUrl())
            .thenReturn(null);

        assertFalse(
            verifier.verifyProfile("testuser")
        );
    }

    @Test
    void shouldRejectBlankCurrentUrl() {

        when(driver.getCurrentUrl())
            .thenReturn("   ");

        assertFalse(
            verifier.verifyProfile("testuser")
        );
    }
}