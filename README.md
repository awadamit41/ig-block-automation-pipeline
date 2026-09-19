# Social Media Account Hygiene Automation

[![Java CI](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml/badge.svg)](https://github.com/awadamit41/Social-Media-Account-Hygiene-Automation/actions/workflows/maven.yml)

![Java](https://img.shields.io/badge/Java-25-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36)
![Selenium](https://img.shields.io/badge/Selenium-4.35.0-43B02A)
![Tests](https://img.shields.io/badge/tests-117%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/JaCoCo-coverage%20checks%20passing-brightgreen)

A Java-based Selenium automation framework for structured social-media account hygiene workflows.

The project focuses on reliable target ingestion, profile navigation, profile verification, action handling, structured result logging, configuration validation, CLI controls, and resilient batch processing.

> **Important:** `--execute` performs real, account-changing block actions against the currently logged-in browser session. `--dry-run` is the safe default and should be used for demonstrations, testing, and CI. See [Safety & Responsible Use](#safety--responsible-use) before running `--execute` against any real account.

---

## Features, Quality & CI

The project uses automated quality gates through GitHub Actions.

- Java 25
- Maven build
- JUnit 5 automated tests
- Mockito-based unit testing (including mocked `WebDriver`/`JavascriptExecutor`/`TakesScreenshot` — no real browser or account required to run the suite)
- Selenium WebDriver
- Checkstyle with zero-violation enforcement
- JaCoCo code coverage
- JaCoCo HTML report published as a CI artifact
- GitHub Actions validation on pushes and pull requests

Local test suite: **117 tests passing**, 0 failures. See the CI badge above for current status; run `mvn clean verify` locally and check `target/site/jacoco/index.html` for exact coverage numbers.

---

### Processing Flow

1. `Main` starts the application.
2. `Application` loads configuration and command-line overrides.
3. `BrowserDriverFactory` creates the configured browser.
4. `TargetLoader` reads and validates target usernames.
5. `TargetProcessor` handles each target independently.
6. `ProfileNavigator` opens the target profile.
7. `ProfileVerifier` confirms the expected profile URL — an unverified profile is never passed to the action layer.
8. `ActionManager` evaluates the configured action mode (dry-run vs. execute) and, in execute mode, delegates to `BlockActionPerformer`.
9. `BlockActionPerformer` performs the browser interaction: opens the profile's options menu, selects Block, confirms in the dialog, and dismisses the follow-up screen if one appears. In dry-run mode, it stops after locating the menu's Block option and never opens the confirm dialog.
10. `ResultLogger` records the processing result (`EXECUTED`, `WOULD_EXECUTE`, `SKIPPED`, or `FAILED`) to `logs/results.csv`.
11. `ProcessingSummary` produces the final execution summary.

> **Safety gates:** Profile verification happens before any action is attempted. Dry-run mode is enforced inside `BlockActionPerformer` itself, not just at the caller — even if upstream configuration is wrong, the confirm/dismiss steps are never reached unless `--execute` is explicitly set. One target's failure (navigation, verification, or action) does not stop the rest of the batch.

---

## Safety & Responsible Use

This tool interacts with a live, third-party platform using non-official browser automation rather than a public API. A few things worth knowing before using `--execute`:

- **Test on a secondary account first.** Run against test targets you control before pointing it at real accounts.
- **`--dry-run` is the default for a reason.** It exercises the full pipeline — navigation, verification, locating the action — without making any account change.
- **Run in small batches**, not the full target list at once, especially on a first run against a given session.
- **UI locators are DOM-dependent and can break** if Instagram changes its interface. `BlockActionPerformer` includes diagnostic logging (per-step console output, element size/location, and screenshots on failure) specifically to make it easy to see where a locator has drifted.
- Automated browser interaction with Instagram outside its official API is against Instagram's Terms of Use. This project is a portfolio/engineering demonstration of a controlled automation pipeline (verification gates, dry-run enforcement, structured logging, failure isolation) — not a recommendation for unattended, large-scale use against accounts you don't control.

---

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
        │       │
        │       └── Block Action Performer (Selenium click sequence)
        │
        └── Result Recorder
        │
        ▼
 Processing Result
        │
        ▼
 Processing Summary
```



## Getting Started

### Prerequisites

- Java 25 (JDK)
- Maven 3.9+
- Microsoft Edge (or update `BrowserDriverFactory`/config for another supported browser)
- EdgeDriver matching your installed Edge version (Selenium Manager typically handles this automatically)

### Build

```bash
mvn clean compile
```

### Run the test suite

```bash
mvn clean verify
```

This runs the full JUnit suite (117 tests), Checkstyle, and JaCoCo coverage — all against mocked `WebDriver`/browser dependencies. No real browser or Instagram account is needed for this step.

### Prepare your target list

Create a `targets.csv` file in the project root with one username per line:

```text
example_user1
example_user2
example_user3
```

Invalid entries (malformed usernames, empty lines) are automatically skipped and reported — they don't stop the rest of the batch from loading.

### Authenticate the browser session

Before running against real profiles, log into the target Instagram account manually in the browser profile this project uses (see `BrowserDriverFactory`/config for the profile path). The automation drives an already-authenticated session — it does not handle login itself.

### Run — dry-run (safe default)

Dry-run exercises the full pipeline (navigation, verification, locating the Block option) without making any account change:

```bash
mvn exec:java "-Dexec.args=--targets targets.csv --dry-run"
```

> **Windows PowerShell users:** the `-D` property must be quoted exactly as shown above (`"-Dexec.args=..."`), with the flags *inside* the quotes. Splitting the quotes differently (e.g. `-Dexec.args="--targets targets.csv --dry-run"`) can cause PowerShell to mis-parse the argument and produce an `Unknown lifecycle phase` error.

### Run — execute (performs real account-changing blocks)

```bash
mvn exec:java "-Dexec.args=--targets targets.csv --execute"
```

Read [Safety & Responsible Use](#safety--responsible-use) before using this. `--dry-run` and `--execute` cannot be combined — the CLI rejects that.

### Alternative: run the built jar directly

If the Maven exec plugin gives you trouble, build and run the classes directly:

```bash
mvn clean package
java -cp target/classes com.hygiene.automation.Main --targets targets.csv --dry-run
```

### Output

- **Console** — per-target progress, including step-by-step diagnostic logging inside `BlockActionPerformer` (element details, click confirmations) and a final processing summary.
- **`logs/results.csv`** — structured, per-target results (`EXECUTED`, `WOULD_EXECUTE`, `SKIPPED`, `FAILED`) with timestamps, for later auditing.
- **`logs/screenshots/`** — a screenshot is captured after the options menu opens and again on any failure, to make DOM/locator issues easy to diagnose without re-running.

### View coverage report

After `mvn clean verify`, open:

```text
target/site/jacoco/index.html
```