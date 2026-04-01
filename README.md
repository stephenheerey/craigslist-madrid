# Craigslist Madrid – Playwright Java Automation Framework

A clean, modular test automation framework for [madrid.craigslist.org](https://madrid.craigslist.org/)
built with **Java 17**, **Playwright**, **TestNG**, and **Allure** reporting.

---

## Review of the Project

I Used the above for the framework for a mix of reasons
  1. Java:       I'm comfortable with Java and have years of experience with it, so was happy to use it
  2. Playwright: was recommended, and I have been doing a small playwright project so I was happy to continue learning on it
  3. TestNG:     similarly to Java, I have used it for years
  4. Allure:     I never used it before, I could have used Cucumber but I wanted to challenge myself in some way too

On the tests, I decided to make sure I covered all scenarios from the brief, so I have added 4 test cases which cover the scenarios.
  * I made some assumptions with the last two tests. both say **such sorting possibilities are available** This I feel is open to interpretation
    * I made the tests on the basis they should be options, but not the only ones.
    * I also added in an ignored test if these should be the only options available, which fails as there are typically more options.
  * I also didn't change the language, it wasn't specified to test in English.
  * I added logging and tried to describe each test case using allures existing syntax
  * I added comments for each method and assertions on every test that should explain if tests fail
  
For the overall flow I used POM and tried to make any methods as useful for other tests as possible.
The SearchResultsPage could be made into a class which could be extended into Housing, but it works fine as is.
I tried to create the smallest locators where possible, but the craigslist page is old and has few distinct identifiers


### Improvements and building onto this:
  * Scope to have tests run in English and Spanish
  * Lots of new tests on the page, viewing results by list, picture only. Using the premade clickable filters. creating saved filters
  

Run options are below and the code is setup on github actions. 




## Project Structure

```
craigslist-tests/
├── pom.xml
└── src/test/
    ├── java/
    │   ├── pages/
    │   │   ├── BasePage.java           # Common Playwright helpers
    │   │   ├── HomePage.java           # Homepage Page Object
    │   │   ├── SearchResultsPage.java  # Search results Page Object
    │   ├── tests/
    │   │   ├── BaseTest.java           # TestNG lifecycle hooks
    │   │   ├── HomepageTest.java       # Homepage tests
    │   └── utils/
    │       ├── BrowserManager.java     # Thread-safe Playwright lifecycle
    │       └── ConfigManager.java      # Config/property loading
    └── resources/
        ├── config.properties           # Runtime configuration
        ├── testng.xml                  # TestNG suite definition
        └── logback-test.xml            # Logging configuration
```

---

## Prerequisites

| Tool      | Version  |
|-----------|----------|
| Java JDK  | 17+      |
| Maven     | 3.8+     |

---

## Setup

### 1. Install dependencies and Playwright browsers

```bash
mvn install -DskipTests
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### 2. (Optional) Edit `config.properties`

```properties
browser=chromium          # chromium | firefox | webkit
headless=true             # true | false
slow.motion.ms=0          # milliseconds between actions (useful for debugging)
screenshot.on.failure=true
```

You can also override any property via JVM system properties:

```bash
mvn test -Dheadless=false -Dbrowser=firefox
```

---

## Running Tests

### Run all tests
```bash
mvn test
```

## Allure Reporting

```bash
# Generate and open the Allure report after a test run
mvn allure:serve
```

Or generate a static report:
```bash
mvn allure:report
# Report is at target/site/allure-maven-plugin/index.html
```

---

## Key Design Decisions

| Pattern | Why |
|---------|-----|
| **Page Object Model** | Separates test logic from UI interaction; maintainable selectors |
| **ThreadLocal browser** | Enables safe parallel test execution |
| **ConfigManager singleton** | Centralised config with CI override via `-D` flags |
| **BaseTest hooks** | Consistent setup/teardown + automatic screenshot on failure |
| **AssertJ** | Fluent, readable assertions with great failure messages |
| **Allure annotations** | Rich HTML reports with feature/story hierarchy |

---

## Extending the Framework

### Add a new page
1. Create `src/test/java/pages/MyNewPage.java` extending `BasePage`
2. Define selectors as `private static final String` constants
3. Implement action and query methods

### Add a new test class
1. Create `src/test/java/tests/MyNewTest.java` extending `BaseTest`
2. Add `@Feature` / `@Story` / `@Test` annotations
3. Register the class in `src/test/resources/testng.xml`
