package ru.tinkoff.page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class MainPage extends AbstractPage {
  private static final String BASE_URL = "https://www.tinkoff.ru";
  private static final By PAYMENTS_LINK = By.xpath("(//a[@href='/payments/'])[2]");
  private static final By SEARCH_INPUT = By.xpath("//input[@class='ui-search-input__input']");
  private static final By SEARCH_RESULTS_LINKS = By.xpath("//div[@class='ui-search-flat']//div[@class='ui-search-flat__title-box']");
  private static final By COMMUNAL_PAYMENTS_LINK = By.xpath("(//a[@href='/payments/kommunalnie-platezhi/'])[2]");
  private static final By KNOW_ARREARS_LINK = By.xpath("//button[@class='ui-button ui-button_failure ui-button_size_xxl']");
  private static final By REGION_LINK = By.xpath("//span[@class='ui-link payment-page__title_inner']");
  private static final By REGIONS_LINKS = By.xpath("//div[@class='ui-scroll ui-regions__layout']//span[@class='ui-link']");
  private static final By PAYMENT_LINKS = By.xpath("//ul[@class='ui-menu ui-menu_icons']/li/span[1]/a");
  private static final By RENDER_PAYMENT_LINK = By.xpath("//span[@class='ui-link ui-menu-second__link']");

  private static final By PROVIDER_PAYER_CODE_INPUT = By.xpath("//input[@name='provider-payerCode']");
  private static final By PROVIDER_PAYER_CODE_ERROR = By.xpath("//input[@name='provider-payerCode']/../../../../div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");
  private static final By PROVIDER_PERIOD_INPUT = By.xpath("//input[@name='provider-period']");
  private static final By PROVIDER_PERIOD_ERROR = By.xpath("//input[@name='provider-period']/../../../../../div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");
  private static final By ACCOUNT_AMOUNT_INPUT = By.xpath("//div[@class='ui-form__row ui-form__row_combination ui-form__row_account-amount']//input");
  private static final By ACCOUNT_AMOUNT_ERROR = By.xpath("//div[@class='ui-form__row ui-form__row_combination ui-form__row_account-amount']//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");


  public MainPage fillProviderPayerCode(String value) {
    driver.getWhenVisible(PROVIDER_PAYER_CODE_INPUT).sendKeys(value);
    return this;
  }

  public String getProviderPayerCodeError() {
    return driver.getWhenVisible(PROVIDER_PAYER_CODE_ERROR)
        .getText();
  }

  public MainPage fillProviderPeriod(String value) {
    driver.getWhenVisible(PROVIDER_PERIOD_INPUT).sendKeys(value);
    return this;
  }

  public String getProviderPeriodError() {
    return driver.getWhenVisible(PROVIDER_PERIOD_ERROR)
        .getText();
  }

  public MainPage fillAccountAmount(String value) {
    driver.getWhenVisible(ACCOUNT_AMOUNT_INPUT).sendKeys(value);
    return this;
  }

  public String getAccountAmountError() {
    WebElement error = driver.getWhenVisible(ACCOUNT_AMOUNT_ERROR);
    driver.scrollToElement(error);
    return error.getText();
  }

  public MainPage open() {
    driver.get(BASE_URL);
    driver.waitForPageToLoad();
    return this;
  }

  public MainPage clickPayments() {
    driver.clickAndWaitForPageToLoad(PAYMENTS_LINK);
    return this;
  }

  public MainPage clickCommunalPayments() {
    driver.clickAndWaitForPageToLoad(COMMUNAL_PAYMENTS_LINK);
    return this;
  }

  public WebElement getKnowArrearsLink() {
    return driver.getWhenVisible(KNOW_ARREARS_LINK);
  }

  public MainPage searchFor(String keyword) {
    driver.getWhenVisible(SEARCH_INPUT).sendKeys(keyword);
    return this;
  }

  public List<WebElement> getSearchResults() {
    driver.getElements(SEARCH_RESULTS_LINKS, 0);
    driver.getPresentElements(SEARCH_RESULTS_LINKS);
    return driver.getVisibleElements(SEARCH_RESULTS_LINKS);
  }

  public List<String> getSearchResultsTitles() {
    return getSearchResults()
        .stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  public MainPage clickSearchResult(String title) {
    getSearchResults().stream().filter(e -> Objects.equals(e.getText(), title)).findFirst().get()
        .click();
    driver.waitForPageToLoad();
    assert getKnowArrearsLink().isDisplayed() : "the page was not fully loaded";
    return this;
  }

  public String getCurrentRegion() {
    return driver.getWhenVisible(REGION_LINK)
        .getText();
  }

  public MainPage clickRegion() {
    driver.clickAndWaitForPageToLoad(REGION_LINK);
    return this;
  }

  public MainPage assureOrSwitchRegion(String expectedRegion) {
    if (!getCurrentRegion().contains(expectedRegion)) {
      clickRegion();
      WebElement element = driver.findElements(REGIONS_LINKS)
          .stream().filter(e -> e.getText().contains(expectedRegion))
          .findFirst().orElseThrow(() -> new NoSuchElementException("Cannot find a region " + expectedRegion));
      element.click();
    }
    String resultRegion = getCurrentRegion();
    assert resultRegion.contains(expectedRegion) : "current region: " + resultRegion;
    return this;
  }

  private List<WebElement> getPayments() {
    return driver.getVisibleElements(PAYMENT_LINKS);
  }

  public List<String> getPaymentTitles() {
    return getPayments()
        .stream()
        .map(e -> e.getAttribute("title"))
        .collect(Collectors.toList());
  }

  public MainPage clickPayment(String title) {
    getPayments().stream().filter(e -> Objects.equals(e.getAttribute("title"), title)).findFirst().get()
        .click();
    driver.waitForPageToLoad();
    return this;
  }

  public MainPage clickRenderPayment() {
    driver.clickAndWaitForPageToLoad(RENDER_PAYMENT_LINK);
    return this;
  }
}
