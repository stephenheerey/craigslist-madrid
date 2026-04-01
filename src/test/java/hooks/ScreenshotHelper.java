package hooks;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotHelper {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotHelper.class);
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String BASE_DIR = "target/screenshots";

    private final Page page;

    public ScreenshotHelper(Page page) {
        this.page = page;
    }

    /** Take a full-page screenshot with a descriptive name + timestamp. */
    public byte[] take(String name) {
        String fileName = sanitise(name) + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
        Path path = Paths.get(BASE_DIR, fileName);
        try {
            Files.createDirectories(path.getParent());
            byte[] bytes = page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(path));
            log.info("Screenshot saved: {}", path);
            return bytes;
        } catch (Exception e) {
            log.warn("Screenshot failed for '{}': {}", name, e.getMessage());
            return new byte[0];
        }
    }

    private String sanitise(String name) {
        return name.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}
