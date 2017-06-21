package ru.tinkoff.payments;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.AbstractTest;
import ru.tinkoff.page.MainPage;

@Test(description = "Тесты раздела /Коммунальные платежи/")
public class CommunalPaymentsTest extends AbstractTest {
  private static final String FIRST_PAYMENT_PROVIDER = "ЖКУ-Москва";
  private String firstPaymentProviderUrl;
  private MainPage mainPage;

  @Test(description = "Перейти на страницу /Платежи/, открыть категорию /Коммунальные платежи/," +
      " выбрать первого из списка поставщика услуг - /" + FIRST_PAYMENT_PROVIDER + "/," +
      " перейти на /Оплатить ЖКУ в Москве/")
  public void switchRegionMoscow() throws Exception {
    mainPage = new MainPage().open()
        .clickPayments()
        .clickCommunalPayments()
        .assureOrSwitchRegion("Москв");

    String firstPaymentProvider = mainPage.getPaymentTitles().stream().findFirst().get();
    assert Objects.equals(firstPaymentProvider, FIRST_PAYMENT_PROVIDER)
        : "unexpected first payment provider: " + firstPaymentProvider;
    mainPage.clickPayment(FIRST_PAYMENT_PROVIDER);

    mainPage.clickRenderPayment();
    firstPaymentProviderUrl = mainPage.getCurrentPageUrl();
  }

  @DataProvider
  public Object[][] invalidProviderPayerCodeCases() {
    return new Object[][] {
        {"\n", "Поле обязательное"},
        {"1\t", "Поле неправильно заполнено"},
        {"12345678901\t", "Поле неправильно заполнено"},
        {"a\t", "Поле неправильно заполнено"},
        {"-\t", "Поле неправильно заполнено"},
    };
  }

  @Test(
      dependsOnMethods = "switchRegionMoscow",
      dataProvider = "invalidProviderPayerCodeCases",
      description = "Проверить сообщения об ошибках для всех типов невалидных значений кода плательщика"
  )
  public void invalidProviderPayerCode(String inputValue, String expectedError) throws Exception {
    checkInvalidFieldValues(expectedError, mainPage.fillProviderPayerCode(inputValue).getProviderPayerCodeError());
  }

  @DataProvider
  public Object[][] invalidProviderPeriodCases() {
    return new Object[][] {
        {"\n", "Поле обязательное"},
        {"1\t", "Поле заполнено некорректно"},
        {"01\t", "Поле заполнено некорректно"},
        {"132018\t", "Поле заполнено некорректно"},
    };
  }

  @Test(
      dependsOnMethods = "invalidProviderPayerCode",
      dataProvider = "invalidProviderPeriodCases",
      description = "Проверить сообщения об ошибках для всех типов невалидных значений периода оплаты"
  )
  public void invalidProviderPeriod(String inputValue, String expectedError) throws Exception {
    checkInvalidFieldValues(expectedError, mainPage.fillProviderPeriod(inputValue).getProviderPeriodError());
  }

  @DataProvider
  public Object[][] invalidAccountAmountCases() {
    return new Object[][] {
        {"\n", "Поле обязательное"},
        {"0\n", "Минимальная сумма перевода - 10 \u20BD"},
        {"9,99\t", "Минимальная сумма перевода - 10 \u20BD"},
        {"15001\t", "Максимальная сумма перевода - 15 000 \u20BD"},
        {"15000,01\t", "Максимальная сумма перевода - 15 000 \u20BD"},
    };
  }

  @Test(
      dependsOnMethods = "invalidProviderPeriod",
      dataProvider = "invalidAccountAmountCases",
      description = "Проверить сообщения об ошибках для всех типов невалидных значений суммы платежа"
  )
  public void invalidAccountAmount(String inputValue, String expectedError) throws Exception {
    checkInvalidFieldValues(expectedError, mainPage.fillAccountAmount(inputValue).getAccountAmountError());
  }

  private void checkInvalidFieldValues(String expectedError, String actualError) {
    assert Objects.equals(actualError, expectedError) : "unexpected error: " + actualError;
    mainPage.refreshPage();
  }

  @Test(
      dependsOnMethods = "invalidAccountAmount",
      description = "Перейти на страницу /Платежи/, в строке поиска поставщиков ввести" +
          " /" + FIRST_PAYMENT_PROVIDER + "/, убедиться, что искомый поставщик - первый в списке," +
          " нажатием на найденный элемент перейти на страницу поставщика, убедиться, что страница совпадает" +
          " со страницей, на которую перешли из рубрикатора")
  public void searchPaymentProvider() throws Exception {
    mainPage
        .clickPayments()
        .searchFor(FIRST_PAYMENT_PROVIDER);

    String firstSearchResultTitle = mainPage.getSearchResultsTitles().get(0);
    assert Objects.equals(firstSearchResultTitle, FIRST_PAYMENT_PROVIDER) :
        "unexpected first title: " + firstSearchResultTitle + " should be: " + FIRST_PAYMENT_PROVIDER;

    mainPage.clickSearchResult(FIRST_PAYMENT_PROVIDER);
    String currentPageUrl = mainPage.getCurrentPageUrl();
    assert Objects.equals(currentPageUrl, firstPaymentProviderUrl) :
        "unexpected page url: " + currentPageUrl + " should be: " + firstPaymentProviderUrl;
  }

  @Test(dependsOnMethods = "searchPaymentProvider")
  public void switchRegionPetersburg() throws Exception {
    mainPage
        .clickPayments()
        .clickCommunalPayments()
        .assureOrSwitchRegion("Петербург");

    List<String> items = mainPage.getPaymentTitles().stream().filter(e -> e.contains(FIRST_PAYMENT_PROVIDER))
        .collect(Collectors.toList());
    assert items.size() == 0 : "should be no such items: " + items;
  }
}