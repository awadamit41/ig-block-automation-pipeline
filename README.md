# Social Media Account Hygiene Automation

[![Java CI](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml/badge.svg)](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml)

![Java](https://img.shields.io/badge/Java-25-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36)
![Selenium](https://img.shields.io/badge/Selenium-4.35.0-43B02A)
![Tests](https://img.shields.io/badge/tests-90%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/JaCoCo-96%25%20instructions-brightgreen)

A Java-based Selenium automation framework for structured social-media account hygiene workflows.

The project focuses on reliable target ingestion, profile navigation, profile verification, dry-run action handling, structured result logging, configuration validation, CLI controls, and resilient batch processing.

> **Important:** Account-changing operations are intentionally disabled in this prototype. `--execute` does not perform real account-changing actions.

---

## Features, Quality & CI

The project uses automated quality gates through GitHub Actions.

- Java 25
- Maven build
- JUnit 5 automated tests
- Mockito-based unit testing
- Selenium WebDriver
- Checkstyle with zero-violation enforcement
- JaCoCo code coverage
- Instruction coverage target: 50% minimum
- Branch coverage target: 50% minimum
- JaCoCo HTML report published as a CI artifact
- GitHub Actions validation on pushes and pull requests

Current local coverage:

- **96% instruction coverage**
- **90% branch coverage**

---

### Processing Flow

1. `Main` starts the application.
2. `Application` loads configuration and command-line overrides.
3. `BrowserDriverFactory` creates the configured browser.
4. `TargetLoader` reads and validates target usernames.
5. `TargetProcessor` handles each target independently.
6. `ProfileNavigator` opens the target profile.
7. `ProfileVerifier` confirms the expected profile URL.
8. `ActionManager` evaluates the configured action mode.
9. `ResultLogger` records the processing result.
10. `ProcessingSummary` produces the final execution summary.

> **Safety:** Account-changing operations are intentionally disabled in this prototype. Dry-run processing verifies navigation and action intent without modifying an account.


## Architecture

The application follows a layered processing pipeline:

<img width="3513" height="1592" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/4a233bdb-bcbe-4426-9f7f-9dd800d03f49" />

```text
CLI / Configuration
        │
        ▼
   Target Loader
        │
        ▼
 Target Processor
        │
        ├── Navigator
        │
        ├── Verifier
        │
        ├── Action Executor
        │
        └── Result Recorder
        │
        ▼
 Processing Result
        │
        ▼
 Processing Summary
