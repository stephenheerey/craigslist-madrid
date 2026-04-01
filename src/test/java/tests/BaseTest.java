package tests;

import com.microsoft.playwright.Page;
import hooks.ScreenshotHelper;
import io.qameta.allure.Attachment;
import listeners.SuiteListener;
import listeners.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.BrowserManager;
import utils.ConfigManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Listeners({TestListener.class, SuiteListener.class})
public abstract class BaseTest {

    protected final Logger           log       = LoggerFactory.getLogger(getClass());
    protected final ConfigManager    config    = ConfigManager.getInstance();

    // Helpers — initialised per test method in setUp()
    protected ScreenshotHelper screenshots;   // static-method helper, kept as reference

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get(config.screenshotDir()));
        Files.createDirectories(Paths.get("target/logs"));
        BrowserManager.initBrowser();
        screenshots = new ScreenshotHelper(page());

        navigateToBaseUrl();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess() && config.screenshotOnFail()) {
            attachScreenshot(screenshots.take(result.getName() + "_FAILED"));
        }
        BrowserManager.closeBrowser();
    }

    protected Page page() {
        return BrowserManager.getPage();
    }

    private void navigateToBaseUrl() {
        log.info("Navigating to {}", config.baseUrl());
        page().navigate(config.baseUrl());
        page().waitForLoadState();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }
}
