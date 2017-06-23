package ru.tinkoff.page;

import ru.tinkoff.util.SharedWebDriver;

public class AbstractPage {
  protected static final String BASE_URL = "https://www.tinkoff.ru";
  protected SharedWebDriver driver = SharedWebDriver.getSharedWebDriver();

  public <T extends AbstractPage> T newPage(Class<T> clazz) {
    T page;
    try {
      page = clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return page;
  }

  public String getCurrentPageUrl() {
    return driver.getCurrentUrl();
  }

  public void refreshPage() {
    driver.navigate().refresh();
    driver.waitForPageToLoad();
  }
}
