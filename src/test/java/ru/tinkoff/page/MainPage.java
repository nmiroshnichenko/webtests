package ru.tinkoff.page;

import org.openqa.selenium.By;
import ru.tinkoff.page.payments.PaymentsPage;

public class MainPage extends AbstractPage {
  private static final By PAYMENTS_LINK = By.xpath("(//a[@href='/payments/'])[2]");

  public MainPage open() {
    driver.get(BASE_URL);
    driver.waitForPageToLoad();
    return this;
  }

  public PaymentsPage clickPayments() {
    driver.clickAndWaitForPageToLoad(PAYMENTS_LINK);
    return new PaymentsPage();
  }
}
