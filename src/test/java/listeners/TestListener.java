package listeners;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.BrowserManager;

import java.io.ByteArrayInputStream;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶  STARTED  : {}", fullName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ PASSED   : {} ({}ms)", fullName(result), elapsed(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ FAILED   : {} ({}ms) — {}", fullName(result), elapsed(result),
                result.getThrowable().getMessage());
        attachScreenshotToAllure(fullName(result));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⏭  SKIPPED  : {}", fullName(result));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void attachScreenshotToAllure(String testName) {
        try {
            byte[] screenshot = BrowserManager.takeScreenshot(testName.replaceAll("[^a-zA-Z0-9_-]", "_"));
            if (screenshot.length > 0) {
                Allure.addAttachment("Screenshot on failure",
                        "image/png", new ByteArrayInputStream(screenshot), ".png");
            }
        } catch (Exception e) {
            log.warn("Could not attach screenshot to Allure report: {}", e.getMessage());
        }
    }

    private String fullName(ITestResult r) {
        return r.getTestClass().getRealClass().getSimpleName() + "#" + r.getName();
    }

    private long elapsed(ITestResult r) {
        return r.getEndMillis() - r.getStartMillis();
    }
}
