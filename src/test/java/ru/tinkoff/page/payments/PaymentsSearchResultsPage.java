package ru.tinkoff.page.payments;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.tinkoff.page.AbstractPage;

public class PaymentsSearchResultsPage extends AbstractPage {
  public static final By SEARCH_RESULTS_LINKS = By.xpath("//div[@class='ui-search-flat']//div[@class='ui-search-flat__title-box']");

  public List<WebElement> getSearchResults() {
    driver.getElements(SEARCH_RESULTS_LINKS, 0);
    return driver.getVisibleElements(SEARCH_RESULTS_LINKS);
  }

  public List<String> getSearchResultsTitles() {
    return getSearchResults()
        .stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  public <T extends AbstractPage> T clickSearchResult(String title, Class<T> clazz) {
    getSearchResults().stream().filter(e -> Objects.equals(e.getText(), title)).findFirst().get()
        .click();
    driver.waitForPageToLoad();
    return newPage(clazz);
  }
}
