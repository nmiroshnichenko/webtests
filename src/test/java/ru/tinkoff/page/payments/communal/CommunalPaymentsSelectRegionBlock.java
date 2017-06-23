package ru.tinkoff.page.payments.communal;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.tinkoff.page.AbstractPage;

public class CommunalPaymentsSelectRegionBlock extends AbstractPage {
  private static final By CURRENT_REGION_LINK = By.xpath("//div[@class='ui-scroll ui-regions__layout']//span[@class='ui-link ui-link_active']");
  private static final By REGIONS_LINKS = By.xpath("//div[@class='ui-scroll ui-regions__layout']//span[@class='ui-link']");

  public String getCurrentRegion() {
    return getCurrentRegionElement().getText();
  }

  public WebElement getCurrentRegionElement() {
    return driver.getWhenVisible(CURRENT_REGION_LINK);
  }

  public CommunalPaymentsPage selectRegion(String expectedRegion) {
    if (getCurrentRegion().equals(expectedRegion)) {
      getCurrentRegionElement().click();
    } else {
      driver.getPresentElements(REGIONS_LINKS);
      WebElement targetRegion = driver.getVisibleElements(REGIONS_LINKS)
          .stream().filter(e -> e.getText().equals(expectedRegion))
          .findFirst().orElseThrow(() -> new NoSuchElementException("Cannot find a region " + expectedRegion));
      targetRegion.click();
    }
    return newPage(CommunalPaymentsPage.class);
  }
}
