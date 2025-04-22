
```markdown
# Petfinder Test Automation Suite

## 1. Introduction

This repository contains an end‑to‑end test automation framework for the Petfinder API and a sample web‑tables application.  
It includes:

- **API Functional Tests** (Rest Assured + JUnit 5)  
- **Performance Tests** (Gatling Scala via Maven plugin)  
- **UI BDD Tests** (Selenium WebDriver + Cucumber JVM + TestNG)  
- **Data‑Driven Scenarios** (CSV‑fed user data)  
- **Containerized Execution** (Docker & Docker Compose)  
- **CI/CD Pipelines** (GitHub Actions)

All code is fully documented with Javadoc and inline comments, structured for readability and maintainability.

---


---

## 2. Setup Instructions

### Prerequisites

- **Java 11+**  
- **Maven 3.8+**  
- **Docker & Docker Compose**  
- **(Optional)** Cucumber Publish Token for online reports  

### Clone & Build

```bash
git clone https://github.com/your‑org/your‑repo.git
cd your‑repo
mvn clean install -DskipTests
```


---

## 3. Running the Tests

### 3.1 API Functional Tests

Run only the Rest Assured tests:

```bash
mvn test -Dtest=com.absa.api.PetfinderRealTest
```

Or run the full suite (API + UI + Perf):

```bash
mvn test
```

### 4.2 Performance Tests (Gatling)

```bash
mvn gatling:test
```

- Reports output: `target/gatling/<simulation-name>/index.html`

### 4.3 UI BDD Tests

#### 4.3.1 Local Execution

Requires a running Selenium server + browser driver:

```bash
export SELENIUM_HUB_URL=http://localhost:4444/wd/hub
mvn test -Dtestng.groups=ui
```

#### 4.3.2 Containerized Execution

1. Start services:

    ```bash
    docker-compose up -d
    ```

2. Execute tests:

    ```bash
    docker-compose run --rm test-runner mvn test
    ```

3. Tear down:

    ```bash
    docker-compose down -v
    ```

- Cucumber HTML report: `target/cucumber-report.html`  
- TestNG/Surefire reports: `target/surefire-reports`

---

## 5. Assumptions & Decisions

- **Page Object Model**: Improves maintainability of UI tests.  
- **Data‑Driven**: Users loaded from CSV, with dynamic usernames to avoid clashes.  
- **Cucumber + TestNG**: Combines BDD readability with TestNG reporting.  
- **Docker Compose**: Ensures consistent test environments (Grid + MySQL).  
- **Single Build Tool**: Maven drives compilation, dependencies, API, UI, and Gatling tests.  

---

## 6. Code Quality & Best Practices

- **Javadoc Comments** on all public classes/methods.  
- **Inline Comments** for complex logic (e.g., retry sleeps, exception handling).  
- **SLF4J Logging** and proper error handling in utilities.  
- **Consistent Naming**: CamelCase for classes, clear locator variables in pages.  
- **Modular Design**: Clean separation between API, UI, performance, and utilities.  

---

## 7. Reports


┌──────────────────────────────────────────────────────────────────────────┐
│ View your Cucumber Report at:                                            │
│ https://reports.cucumber.io/reports/30e8334c-38a8-40d6-8805-8ac323b0121f │
│                                                                          │
│ This report will self-destruct in 24h.                                   │
│ Keep reports forever: https://reports.cucumber.io/profile                │
└──────────────────────────────────────────────────────────────────────────┘

