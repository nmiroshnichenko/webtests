package ru.tinkoff.page.payments;

import org.openqa.selenium.By;
import ru.tinkoff.page.AbstractPage;
import ru.tinkoff.page.payments.communal.CommunalPaymentsPage;

public class PaymentsPage extends AbstractPage {
  private static final By COMMUNAL_PAYMENTS_LINK = By.xpath("(//a[@href='/payments/kommunalnie-platezhi/'])[2]");
  private static final By SEARCH_INPUT = By.xpath("//input[@class='ui-search-input__input']");

  public CommunalPaymentsPage clickCommunalPayments() {
    driver.clickAndWaitForPageToLoad(COMMUNAL_PAYMENTS_LINK);
    return new CommunalPaymentsPage();
  }

  public PaymentsPage searchFor(String keyword) {
    driver.getWhenVisible(SEARCH_INPUT).sendKeys(keyword);
    return this;
  }
}
