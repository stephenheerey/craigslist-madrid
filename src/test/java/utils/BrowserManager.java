package utils;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class BrowserManager {

    private static final Logger log = LoggerFactory.getLogger(BrowserManager.class);
    private static final ThreadLocal<Playwright> playwrightTL = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserTL = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextTL = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageTL = new ThreadLocal<>();

    private static final ConfigManager config = ConfigManager.getInstance();

    public static void initBrowser() {
        log.info("Initialising Playwright browser: {}", config.browser());

        Playwright playwright = Playwright.create();
        playwrightTL.set(playwright);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(config.headless())
                .setSlowMo(config.slowMotion());

        Browser browser = switch (config.browser().toLowerCase()) {
            case "firefox"  -> playwright.firefox().launch(launchOptions);
            case "webkit"   -> playwright.webkit().launch(launchOptions);
            default         -> playwright.chromium().launch(launchOptions);
        };
        browserTL.set(browser);

        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(1440, 900)
                .setLocale("en-US")
                .setIgnoreHTTPSErrors(true);

        if (config.recordVideo()) {
            contextOptions.setRecordVideoDir(Paths.get(config.videoDir()));
        }

        BrowserContext context = browser.newContext(contextOptions);
        context.setDefaultTimeout(config.defaultTimeout());
        context.setDefaultNavigationTimeout(config.navigationTimeout());
        contextTL.set(context);

        Page page = context.newPage();
        pageTL.set(page);

        log.info("Browser ready");
    }

    public static Page getPage()           { return pageTL.get(); }

    public static void closeBrowser() {
        try {
            if (pageTL.get()    != null) pageTL.get().close();
            if (contextTL.get() != null) contextTL.get().close();
            if (browserTL.get() != null) browserTL.get().close();
            if (playwrightTL.get() != null) playwrightTL.get().close();
            log.info("Browser closed");
        } finally {
            pageTL.remove();
            contextTL.remove();
            browserTL.remove();
            playwrightTL.remove();
        }
    }

    public static byte[] takeScreenshot(String name) {
        try {
            Page page = pageTL.get();
            if (page == null) return new byte[0];
            byte[] bytes = page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(Paths.get(config.screenshotDir(), name + ".png")));
            log.info("Screenshot saved: {}.png", name);
            return bytes;
        } catch (Exception e) {
            log.warn("Could not take screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }
}
