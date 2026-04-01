package pages;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {

    protected final Page page;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        log.debug("Clicking: {}", selector);
        page.locator(selector).click();
    }
}
