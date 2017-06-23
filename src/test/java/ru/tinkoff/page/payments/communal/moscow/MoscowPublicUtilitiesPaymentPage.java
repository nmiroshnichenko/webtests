package ru.tinkoff.page.payments.communal.moscow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MoscowPublicUtilitiesPaymentPage extends MoscowPublicUtilitiesPageHeaderBlock {
  private static final By PROVIDER_PAYER_CODE_INPUT = By.xpath("//input[@name='provider-payerCode']");
  private static final By PROVIDER_PAYER_CODE_ERROR = By.xpath("//input[@name='provider-payerCode']/../../../../div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");
  private static final By PROVIDER_PERIOD_INPUT = By.xpath("//input[@name='provider-period']");
  private static final By PROVIDER_PERIOD_ERROR = By.xpath("//input[@name='provider-period']/../../../../../div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");
  private static final By ACCOUNT_AMOUNT_INPUT = By.xpath("//div[@class='ui-form__row ui-form__row_combination ui-form__row_account-amount']//input");
  private static final By ACCOUNT_AMOUNT_ERROR = By.xpath("//div[@class='ui-form__row ui-form__row_combination ui-form__row_account-amount']//div[@class='ui-form-field-error-message ui-form-field-error-message_ui-form']");


  public MoscowPublicUtilitiesPaymentPage fillProviderPayerCode(String value) {
    driver.getWhenVisible(PROVIDER_PAYER_CODE_INPUT).sendKeys(value);
    return this;
  }

  public String getProviderPayerCodeError() {
    return driver.getWhenVisible(PROVIDER_PAYER_CODE_ERROR)
        .getText();
  }

  public MoscowPublicUtilitiesPaymentPage fillProviderPeriod(String value) {
    driver.getWhenVisible(PROVIDER_PERIOD_INPUT).sendKeys(value);
    return this;
  }

  public String getProviderPeriodError() {
    return driver.getWhenVisible(PROVIDER_PERIOD_ERROR)
        .getText();
  }

  public MoscowPublicUtilitiesPaymentPage fillAccountAmount(String value) {
    driver.getWhenVisible(ACCOUNT_AMOUNT_INPUT).sendKeys(value);
    return this;
  }

  public String getAccountAmountError() {
    WebElement error = driver.getWhenVisible(ACCOUNT_AMOUNT_ERROR);
    driver.scrollToElement(error);
    return error.getText();
  }
}
