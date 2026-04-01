package pages;

import com.microsoft.playwright.Page;


public class HomePage extends BasePage {

    private static final String HOUSING_LINK = "a[data-cat='hhh']";

    public HomePage(Page page) {
        super(page);
    }


    //clicks the housing link on the home page and waits for it to load
    public SearchResultsPage clickHousing() {
        click(HOUSING_LINK);
        page.waitForLoadState();
        return new SearchResultsPage(page);
    }
}
