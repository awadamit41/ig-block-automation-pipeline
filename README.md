# Social Media Hygiene Automation

Java + Maven + Selenium project for a controlled social-media account hygiene workflow.

## Current milestone
This starter project only verifies the Java/Maven project setup. It does not perform blocking actions.

## Requirements
- Java 17+
- Maven 3.8+
- Google Chrome
- VS Code (recommended)

## Run

From the project directory:

```bash
mvn clean compile
mvn exec:java
```

Expected output includes:

`Project initialized successfully.`

## Planned modules
- Browser/session manager
- Profile navigator
- Target CSV loader
- Profile verification
- Controlled action manager
- Result logger
- Screenshot-on-error
- Dry-run/manual-confirmation mode

The automation should not attempt to bypass CAPTCHAs, rate limits, fingerprinting, or other anti-abuse controls.
