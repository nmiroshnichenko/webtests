package ru.tinkoff;

import org.testng.annotations.AfterClass;
import ru.tinkoff.util.SharedWebDriver;

public abstract class AbstractTest {

  @AfterClass(alwaysRun = true)
  protected void shutdownDriver() {
    SharedWebDriver.stopWebDriver(SharedWebDriver.getCurrentDriver());
  }
}
