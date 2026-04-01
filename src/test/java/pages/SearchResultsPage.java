package pages;

import com.microsoft.playwright.Page;

import java.util.List;

public class SearchResultsPage extends BasePage {

    // ── Selectors ─────────────────────────────────────────────────────────────
    private static final String RESULT_ROWS             = ".cl-search-result";
    private static final String RESULT_PRICES           = ".priceinfo";
    private static final String SEARCH_INPUT            = "div[class*='cl-query-with-search'] input";
    private static final String EXECUTE_SEARCH          = "button[type=submit]";
    private static final String PRICE_HIGHEST_BUTTON    = "button[class*='price-desc']";
    private static final String PRICE_LOWEST_BUTTON     = "button[class*='price-asc']";
    private static final String SORT_BY                 = "div[class*='cl-search-sort-mode'] button";
    private static final String SORT_BY_OPTIONS         = "div.items button[class*='cl-search-sort-mode'] span.label";

    public SearchResultsPage(Page page) {
        super(page);
    }


    //this uses the sorting dropdown on the page to change the order of the results
    public void sortResults(String sortingType){
        log.info("Sorting Results");
        page.locator(RESULT_ROWS).first().waitFor();
        page.locator(SORT_BY).click();
        if(sortingType.equals("highest")){
            page.locator(PRICE_HIGHEST_BUTTON).waitFor();
            page.locator(PRICE_HIGHEST_BUTTON).click();
        } else if (sortingType.equals("lowest")){
            page.locator(PRICE_LOWEST_BUTTON).waitFor();
            page.locator(PRICE_LOWEST_BUTTON).click();
        } else {
            throw new Error("Invalid Sorting Type Chosen");
        }
        page.waitForLoadState();
    }

    //checks after sorting results by highest price, that the results are in correct order
    public boolean checkPricesHigh(){
        log.info("validating that the prices are sorted from highest to lowest");
        page.locator(RESULT_ROWS).first().waitFor();
        List<String> rawPrices = page.locator(RESULT_PRICES).allInnerTexts();

        List<Integer> prices = getNumbersForPrices(rawPrices);

        for(int i = 0; i < prices.size() - 1;i++){
            if(prices.get(i) < prices.get(i + 1)){
                return false;
            }
        }

        return true;
    }

    //checks after sorting results by lowest price, that the results are in correct order
    public boolean checkPricesLow(){
        log.info("validating that the prices are sorted from lowest to highest");
        page.locator(RESULT_ROWS).first().waitFor();
        List<String> rawPrices = page.locator(RESULT_PRICES).allInnerTexts();

        List<Integer> prices = getNumbersForPrices(rawPrices);

        for(int i = 0; i < prices.size() - 1;i++){
            if(prices.get(i) > prices.get(i + 1)){
                return false;
            }
        }

        return true;
    }


    //helper method which takes all values for the options to sort the page by
    public List<String> sortOptions(){
        log.info("Retrieving sort options");
        page.locator(RESULT_ROWS).first().waitFor();
        page.locator(SORT_BY).click();

        return page.locator(SORT_BY_OPTIONS).allInnerTexts();
    }

    //method to enter in a phrase into the search bar and search
    public void searchUsingPhrase(String phrase){
        log.info("Searching Page using Phrase: {}", phrase);
        page.locator(SEARCH_INPUT).fill(phrase);
        page.locator(EXECUTE_SEARCH).click();
    }

    //helper method taking in a list of Strings for all prices in the results, and returning it as a list of integers
    public List<Integer> getNumbersForPrices(List<String> rawPrices){
        return rawPrices.stream()
                .filter(p -> p != null && !p.isBlank())
                .map(p -> p.replaceAll("[^0-9]", ""))
                .filter(p -> !p.isEmpty())
                .map(Integer::parseInt)
                .toList();
    }
}
