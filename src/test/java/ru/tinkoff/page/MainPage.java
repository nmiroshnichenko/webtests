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
  private static final By COMMUNAL_PAYMENTS_LINK = By.xpath("(//a[@href='/payments/kommunalnie-platezhi/'])[2]");
  private static final By REGION_LINK = By.xpath("(//span[@class='ui-link payment-page__title_inner'])[1]");
  private static final By REGIONS_LINKS = By.xpath("//div[@class='ui-scroll ui-regions__layout']//span[@class='ui-link']");
  private static final By PAYMENT_LINKS = By.xpath("//ul[@class='ui-menu ui-menu_icons']/li[@class='ui-menu__item ui-menu__item_icons']");
  private static final By RENDER_PAYMENT_LINK = By.xpath("//span[@class='ui-link ui-menu-second__link']");

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

  public List<WebElement> getPayments(int number) {
    return driver.getVisibleElements(PAYMENT_LINKS, number);
  }

  public List<String> getPaymentTitles(int number) {
    return getPayments(number).stream().map(e -> e.getAttribute("title")).collect(Collectors.toList());
  }

  public MainPage clickPayment(String title) {
    getPayments(7).stream().filter(e -> Objects.equals(e.getAttribute("title"), title)).findFirst().get()
        .click();
    driver.waitForPageToLoad();
    return this;
  }

  public MainPage clickRenderPayment() {
    driver.clickAndWaitForPageToLoad(RENDER_PAYMENT_LINK);
    return this;
  }
}
