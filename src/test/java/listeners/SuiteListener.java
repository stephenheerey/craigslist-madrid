package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SuiteListener implements ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite suite) {
        log.info("========================================================");
        log.info("  Suite starting : {}", suite.getName());
        log.info("  Time           : {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("========================================================");
        createOutputDirectories();
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed  = suite.getResults().values().stream()
                .mapToInt(r -> r.getTestContext().getPassedTests().size()).sum();
        int failed  = suite.getResults().values().stream()
                .mapToInt(r -> r.getTestContext().getFailedTests().size()).sum();
        int skipped = suite.getResults().values().stream()
                .mapToInt(r -> r.getTestContext().getSkippedTests().size()).sum();

        log.info("========================================================");
        log.info("  Suite finished : {}", suite.getName());
        log.info("  ✅ Passed  : {}", passed);
        log.info("  ❌ Failed  : {}", failed);
        log.info("  ⏭  Skipped : {}", skipped);
        log.info("========================================================");
    }

    private void createOutputDirectories() {
        try {
            Files.createDirectories(Paths.get("target/screenshots"));
            Files.createDirectories(Paths.get("target/logs"));
            Files.createDirectories(Paths.get("target/allure-results"));
        } catch (IOException e) {
            log.warn("Could not pre-create output directories: {}", e.getMessage());
        }
    }
}
