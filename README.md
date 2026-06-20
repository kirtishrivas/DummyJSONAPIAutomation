# DummyJSON API Automation Framework

A professional, enterprise-grade API Test Automation Framework built using **Java**, **RestAssured**, and **Cucumber BDD**. This project features a fully automated Continuous Integration (CI) pipeline powered by **GitHub Actions** to execute automated regression suites in the cloud on every code push.

---

## 🚀 Key Features
* **BDD Framework Structure:** Test scenarios written in plain, human-readable Gherkin syntax.
* **Robust Assertions:** High-coverage verification of API response status codes, headers, and complex JSON payloads.
* **CI/CD Integration:** Automated cloud execution engine running on Ubuntu environments with Java 17.
* **Reporting Architecture:** Generates comprehensive Cucumber HTML execution reports containing detailed test step timings and pass/fail metrics.

---

## 🧰 Tech Stack
* **Language:** Java 17
* **API Testing Library:** RestAssured
* **Test Runner Framework:** JUnit / Cucumber JVM
* **Build Management Tool:** Maven
* **CI/CD Engine:** GitHub Actions

---

## 📁 Project Directory Layout
```text
DummyJSONAPIAutomation/
│
├── .github/workflows/       # GitHub Actions CI workflow configuration
├── src/test/java/           # Automation source code
│   ├── stepDefinitions/     # Step definition implementations
│   └── runners/             # TestRunner configuration classes
│
├── src/test/resources/      # Test artifacts
│   └── features/            # Cucumber Gherkin feature files
│
├── pom.xml                  # Maven project dependencies & build plugins
└── README.md                # Project documentation