package ru.tinkoff.page.payments.communal.moscow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.tinkoff.page.AbstractPage;

public class MoscowPublicUtilitiesPageHeaderBlock extends AbstractPage {
  private static final By KNOW_ARREARS_LINK = By.xpath("//button[@class='ui-button ui-button_failure ui-button_size_xxl']");
  private static final By RENDER_PAYMENT_LINK = By.xpath("//span[@class='ui-link ui-menu-second__link']");

  public WebElement getKnowArrearsLink() {
    return driver.getWhenVisible(KNOW_ARREARS_LINK);
  }

  public MoscowPublicUtilitiesPaymentPage clickRenderPayment() {
    driver.clickAndWaitForPageToLoad(RENDER_PAYMENT_LINK);
    return newPage(MoscowPublicUtilitiesPaymentPage.class);
  }
}
