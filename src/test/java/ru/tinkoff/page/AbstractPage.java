package ru.tinkoff.page;

import ru.tinkoff.util.SharedWebDriver;

public class AbstractPage {
  protected static SharedWebDriver driver = SharedWebDriver.getSharedWebDriver();

  public String getCurrentPageUrl() {
    return driver.getCurrentUrl();
  }

  public void refreshPage() {
    driver.navigate().refresh();
    driver.waitForPageToLoad();
  }
}
