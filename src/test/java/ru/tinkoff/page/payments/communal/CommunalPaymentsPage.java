package ru.tinkoff.page.payments.communal;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.tinkoff.page.AbstractPage;

public class CommunalPaymentsPage extends AbstractPage {
  private static final By SELECT_REGION_LINK = By.xpath("//span[@class='ui-link payment-page__title_inner']");
  private static final By PAYMENT_PROVIDERS_LINKS = By.xpath("//ul[@class='ui-menu ui-menu_icons']/li/span[1]/a");

  public CommunalPaymentsSelectRegionBlock openSelectRegionBlock() {
    driver.clickWhenReady(SELECT_REGION_LINK);
    return newPage(CommunalPaymentsSelectRegionBlock.class);
  }

  public List<WebElement> getPaymentProviders() {
    return driver.getVisibleElements(PAYMENT_PROVIDERS_LINKS);
  }

  public List<String> getPaymentTitles() {
    return getPaymentProviders()
        .stream()
        .map(e -> e.getAttribute("title"))
        .collect(Collectors.toList());
  }

  public <T extends AbstractPage> T openPaymentProviderPage(String title, Class<T> clazz) {
    getPaymentProviders().stream().filter(e -> Objects.equals(e.getAttribute("title"), title)).findFirst().get()
        .click();
    driver.waitForPageToLoad();
    return newPage(clazz);
  }
}
