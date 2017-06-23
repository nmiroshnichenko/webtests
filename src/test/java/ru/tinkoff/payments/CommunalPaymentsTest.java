package ru.tinkoff.payments;

import static org.assertj.core.api.Assertions.assertThat;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.AbstractTest;
import ru.tinkoff.page.MainPage;
import ru.tinkoff.page.payments.PaymentsSearchResultsPage;
import ru.tinkoff.page.payments.communal.CommunalPaymentsPage;
import ru.tinkoff.page.payments.communal.moscow.MoscowPublicUtilitiesPageHeaderBlock;
import ru.tinkoff.page.payments.communal.moscow.MoscowPublicUtilitiesPaymentPage;

@Test(description = "Тесты раздела /Коммунальные платежи/")
public class CommunalPaymentsTest extends AbstractTest {
  private static final String FIRST_PAYMENT_PROVIDER = "ЖКУ-Москва";
  private String firstPaymentProviderUrl;

  @Test(description = "Перейти на страницу /Платежи/, открыть категорию /Коммунальные платежи/," +
      " выбрать первого из списка поставщика услуг - /" + FIRST_PAYMENT_PROVIDER + "/," +
      " перейти на /Оплатить ЖКУ в Москве/")
  public void switchRegionMoscow() throws Exception {
    CommunalPaymentsPage communalPaymentsPage = new MainPage().open()
        .clickPayments()
        .clickCommunalPayments()
        .openSelectRegionBlock()
        .selectRegion("г. Москва");

    String firstPaymentProvider = communalPaymentsPage.getPaymentTitles().stream().findFirst().get();
    assertThat(firstPaymentProvider).as("first payment provider").isEqualTo(FIRST_PAYMENT_PROVIDER);

    firstPaymentProviderUrl = communalPaymentsPage
        .openPaymentProviderPage(FIRST_PAYMENT_PROVIDER, MoscowPublicUtilitiesPageHeaderBlock.class)
        .clickRenderPayment()
        .getCurrentPageUrl();
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
    checkInvalidFieldValues(
        expectedError,
        new MoscowPublicUtilitiesPaymentPage().fillProviderPayerCode(inputValue).getProviderPayerCodeError()
    );
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
    checkInvalidFieldValues(
        expectedError,
        new MoscowPublicUtilitiesPaymentPage().fillProviderPeriod(inputValue).getProviderPeriodError()
    );
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
    checkInvalidFieldValues(
        expectedError,
        new MoscowPublicUtilitiesPaymentPage().fillAccountAmount(inputValue).getAccountAmountError()
    );
  }

  private void checkInvalidFieldValues(String expectedError, String actualError) {
    assertThat(actualError).as("error message").isEqualTo(expectedError);
    new MoscowPublicUtilitiesPaymentPage().refreshPage();
  }

  @Test(
      dependsOnMethods = "invalidAccountAmount",
      description = "Перейти на страницу /Платежи/, в строке поиска поставщиков ввести" +
          " /" + FIRST_PAYMENT_PROVIDER + "/, убедиться, что искомый поставщик - первый в списке," +
          " нажатием на найденный элемент перейти на страницу поставщика, убедиться, что страница совпадает" +
          " со страницей, на которую перешли из рубрикатора")
  public void searchPaymentProvider() throws Exception {
    new MainPage()
        .clickPayments()
        .searchFor(FIRST_PAYMENT_PROVIDER);

    String firstSearchResultTitle = new PaymentsSearchResultsPage().getSearchResultsTitles().get(0);
    assertThat(firstSearchResultTitle).as("first search result title").isEqualTo(FIRST_PAYMENT_PROVIDER);

    MoscowPublicUtilitiesPageHeaderBlock moscowPublicUtilitiesPage = new PaymentsSearchResultsPage()
        .clickSearchResult(FIRST_PAYMENT_PROVIDER, MoscowPublicUtilitiesPageHeaderBlock.class);
    assertThat(moscowPublicUtilitiesPage.getKnowArrearsLink().isDisplayed())
        .as("know arrears link is displayed").isTrue();
    String currentPageUrl = moscowPublicUtilitiesPage.getCurrentPageUrl();
    assertThat(currentPageUrl).as("current page url").isEqualTo(firstPaymentProviderUrl);
  }

  @Test(dependsOnMethods = "searchPaymentProvider")
  public void switchRegionPetersburg() throws Exception {
    CommunalPaymentsPage communalPaymentsPage = new MainPage()
        .clickPayments()
        .clickCommunalPayments()
        .openSelectRegionBlock()
        .selectRegion("г. Санкт-Петербург");

     assertThat(communalPaymentsPage.getPaymentTitles()).as("Petersburg payment providers")
        .doesNotContain(FIRST_PAYMENT_PROVIDER);
  }
}