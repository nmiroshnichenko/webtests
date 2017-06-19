package ru.tinkoff;

import org.testng.annotations.Test;
import ru.tinkoff.page.MainPage;

@Test
public class PaymentsTest extends AbstractTest {

  public void testPayments() throws Exception {
    MainPage mainPage = new MainPage().open()
        .clickPayments()
        .clickCommunalPayments()
        .assureOrSwitchRegion("Москв");

    String firstPayment = mainPage.getPaymentTitles(1).stream().findFirst().get();
    mainPage.clickPayment(firstPayment)
        .clickRenderPayment();
  }
}