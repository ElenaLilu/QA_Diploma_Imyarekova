package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CardsInfo;
import ru.netology.data.DbUtils;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;

@Feature("Тестируем покупку тура по карте")
public class PaymentTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        // Configuration.headless = true;
        open("http://localhost:8080");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        DbUtils.deleteTables();
    }

    //Проверка карты со статусом APPROVED
    @Test
    void shouldPaymentWithApprovedCard() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.successMessage();
        String actual = DbUtils.getStatusPayment();
        assertEquals("APPROVED", actual);
    }

    //Проверка карты со статусом DECLINED
    @Test
    void shouldPaymentWithDeclinedCard() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getDeclinedCardNumber(), getMonth(0), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.failMessage();
        String actual = DbUtils.getStatusPayment();
        assertEquals("DECLINED", actual);
    }

    //Проверка карты с несуществующим номером
    @Test
    void shouldPaymentWithInvalidCardNumber() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getInvalidCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.failMessage();
    }

    //Проверка карты, состоящей из 12 цифр
    @Test
    void shouldPaymentWithInvalidCardNumberShort() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getInvalidShortCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Проверка карты, номер которой состоит из букв
    @Test
    void shouldPaymentWithInvalidCardNumberOfLetters() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getCardNumberWithLetters(), getMonth(10), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Проверка поля карты с пустым значением
    @Test
    void shouldPaymentEmptyField() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                null, getMonth(10), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }

    //Оплата картой, срок окончания которой - год, предшесвующий текущему
    @Test
    void shouldPaymentExpiredCard() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(0), getYear(-1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.cardExpiredMessage();
    }

    //Оплата картой, срок окончания которой более, чем через 5 лет относительно текущего года
    @Test
    void shouldPaymentCardExpirationPlusFiveYears() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(0), getYear(6), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, где срок окончания указан буквами
    @Test
    void shouldPaymentCardYearIndicatedLetters() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getInvalidYear(), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, где год срока окончания не указан
    @Test
    void shouldPaymentCardWithEmptyYear() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), null, getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }

    //Оплата картой, у которой месяц превышает 12
    @Test
    void shouldPaymentIncorrectCardExpirationDate() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonthInvalidPeriod(), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, месяц которой указан: 00
    @Test
    void shouldPaymentCardInvalidMonth() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonth(), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, месяц которой указан буквами
    @Test
    void shouldPaymentCardMonthWithLetters() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonth2(), getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, где поле месяца не заполнено
    @Test
    void shouldPaymentEmptyFieldMonth() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), null, getYear(1), getHolder(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }


    //Невалидные данные о владельце: имя и фамилия на кириллице
    @Test
    void shouldPaymentInvalidHolderCard() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardCyrillic(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидные данные о владельце: цифры в имени
    @Test
    void shouldPaymentInvalidHolderCardWithNumbers() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardWithNumbers(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидные данные о владельце: введена только одна буква
    @Test
    void shouldPaymentInvalidHolderCardWithOneLetter() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardOneLetterName(), getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидные данные о владельце: пустое поле
    @Test
    void shouldPaymentEmptyHolder() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), null, getCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }


    //Невалидный код CVV: ввод двух цифр
    @Test
    void shouldPaymentCardInvalidCvv() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvv());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидный код CVV: ввод одной цифры
    @Test
    void shouldPaymentCardInvalidCvv2() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvv2());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидный код CVV: ввод трёх нулей
    @Test
    void shouldPaymentCardInvalidCvvZero() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvvZero());
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Невалидный код CVV: пустое поле
    @Test
    void shouldPaymentCardInvalidEmptyCvv() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), null);
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }

    //Отправка пустой формы покупки тура картой
    @Test
    void shouldPaymentEmptyAllField() {
        var startPage = new MainPage();
        CardsInfo card = new CardsInfo(
                null, null, null, null, null);
        var paymentPage = startPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }
}
