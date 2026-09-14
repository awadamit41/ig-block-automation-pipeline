# Social Media Account Hygiene Automation

[![Java CI](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml/badge.svg)](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml)

![Java](https://img.shields.io/badge/Java-25-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36)
![Selenium](https://img.shields.io/badge/Selenium-4.35.0-43B02A)
![Tests](https://img.shields.io/badge/tests-79%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/JaCoCo-74%25%20instructions-brightgreen)

A Java-based Selenium automation framework for structured social-media account hygiene workflows.

The project focuses on reliable target ingestion, profile navigation, profile verification, dry-run action handling, structured result logging, configuration validation, CLI controls, and resilient batch processing.

> **Important:** Account-changing operations are intentionally disabled in this prototype. `--execute` does not perform real account-changing actions.

---

## Features

- Java 25 application
- Maven-based project
- Selenium WebDriver with Microsoft Edge
- Persistent Edge user profile
- CSV-based target ingestion
- Duplicate target removal
- Username validation
- Profile navigation
- Profile URL verification
- Dry-run action layer
- Structured CSV result logging
- Processing status and summary reporting
- Command-line overrides
- Configuration validation
- Per-target failure isolation
- Dependency-injected processing components
- JUnit 5 unit tests

---

## Architecture

The application follows a layered processing pipeline:

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
