package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Housing")
public class HousingTest extends BaseTest {

    @Test(description = "Sorting Housing page results by highest price")
    @Story("Result Sorting")
    @Description("Verify that the results on the Housing Page sorted by highest price works as expected")
    public void testHousingHighestPriceFilterNavigation() {
        HomePage homePage = new HomePage(page());
        SearchResultsPage results = homePage.clickHousing();
        results.sortResults("highest");

        assertThat(results.checkPricesHigh()).as("All results should be ordered by Highest price").isTrue();
    }

    @Test(description = "Sorting Housing page results by lowest price")
    @Story("Result Sorting")
    @Description("Verify that the results on the Housing page sorted by lowest price works as expected")
    public void testHousingLowestPriceFilterNavigation() {
        HomePage homePage = new HomePage(page());
        SearchResultsPage results = homePage.clickHousing();
        results.sortResults("lowest");

        assertThat(results.checkPricesLow()).as("All results should be ordered by lowest price").isTrue();
    }

    @Test(description = "Highest, Lowest and newest are sorting options by default")
    @Story("Result Sorting")
    @Description("Verify that on the default housing page, you can sort by highest price, lowest price and newest")
    public void testDefaultHousingFilters() {
        HomePage homePage = new HomePage(page());
        SearchResultsPage results = homePage.clickHousing();
        List<String> expectedFilterOptions = Arrays.asList("$$$ → ﹩","﹩→ $$$", "+ nuevo");
        assertThat(results.sortOptions()).as("Highest Price, Lowest Price and Newest are available options").containsAll(expectedFilterOptions);
    }

    @Ignore
    @Test(description = "Highest, Lowest and newest are the ONLY sorting options by default")
    @Story("Result Sorting")
    @Description("Verify that on the default housing page, you can only sort by highest price, lowest price and newest")
    public void testDefaultHousingFiltersExplicit() {
        HomePage homePage = new HomePage(page());
        SearchResultsPage results = homePage.clickHousing();

        List<String> expectedFilterOptions = Arrays.asList("$$$ → ﹩","﹩→ $$$", "+ nuevo");
        assertThat(expectedFilterOptions).as("Highest Price, Lowest Price and Newest are available options").containsAll(results.sortOptions());
    }

    @Test(description = "Highest, Lowest, upcoming, relevant and newest are sorting options after searching")
    @Story("Result Sorting")
    @Description("Verify that after searching on the housing page, you can sort by highest price, lowest price, relevant, upcoming and newest")
    public void testSearchHousingFilters() {
        HomePage homePage = new HomePage(page());
        SearchResultsPage results = homePage.clickHousing();

        List<String> expectedFilterOptions = Arrays.asList("$$$ → ﹩","﹩→ $$$", "+ nuevo","Relevancia","próximo");
        results.searchUsingPhrase("House");
        assertThat(results.sortOptions()).as("Highest Price, Lowest Price, relevant, upcoming and Newest are available options").containsAll(expectedFilterOptions);
    }
}
