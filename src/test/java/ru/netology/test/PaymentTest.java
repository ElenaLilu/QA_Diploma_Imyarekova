package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.successMessage();
        String actual = DbUtils.getStatusPayment();
        assertEquals("APPROVED", actual);
    }

    //Проверка карты со статусом DECLINED
    @Test
    void shouldPaymentWithDeclinedCard() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getDeclinedCardNumber(), getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.failMessage();
        String actual = DbUtils.getStatusPayment();
        assertEquals("DECLINED", actual);
    }

    //Проверка карты с несуществующим номером
    @Test
    void shouldPaymentWithInvalidCardNumber() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getInvalidCardNumber(), getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.failMessage();
    }

    //Проверка карты, состоящей из 12 цифр
    @Test
    void shouldPaymentWithInvalidCardNumberShort() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getInvalidShortCardNumber(), getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Проверка карты, номер которой состоит из букв
    @Test
    void shouldPaymentWithInvalidCardNumberOfLetters() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getCardNumberWithLetters(), getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Проверка поля карты с пустым значением
    @Test
    void shouldPaymentEmptyField() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                null, getMonth(7), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Оплата картой, срок окончания которой - год, предшесвующий текущему
    @Test
    void shouldPaymentExpiredCard() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(0), getYear(-1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.cardExpiredMessage();
    }

    //Оплата картой, срок окончания которой более, чем через 5 лет относительно текущего года
    @Test
    void shouldPaymentCardExpirationPlusFiveYears() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(0), getYear(6), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, где срок окончания указан буквами
    @Test
    void shouldPaymentCardYearIndicatedLetters() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getInvalidYear(), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, где год срока окончания не указан
    @Test
    void shouldPaymentCardWithEmptyYear() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), null, getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Оплата картой, у которой месяц превышает 12
    @Test
    void shouldPaymentIncorrectCardExpirationDate() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonthInvalidPeriod(), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, месяц которой указан: 00
    @Test
    void shouldPaymentCardInvalidMonth() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonth(), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongTermMessage();
    }

    //Оплата картой, месяц которой указан буквами
    @Test
    void shouldPaymentCardMonthWithLetters() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getInvalidMonth2(), getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Оплата картой, где поле месяца не заполнено
    @Test
    void shouldPaymentEmptyFieldMonth() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), null, getYear(1), getHolder(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }


    //Невалидные данные о владельце: имя и фамилия на кириллице
    @Test
    void shouldPaymentInvalidHolderCard() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getInvalidHolderCardCyrillic(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидные данные о владельце: цифры в имени
    @Test
    void shouldPaymentInvalidHolderCardWithNumbers() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getInvalidHolderCardWithNumbers(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидные данные о владельце: введена только одна буква
    @Test
    void shouldPaymentInvalidHolderCardWithOneLetter() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getInvalidHolderCardOneLetterName(), getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидные данные о владельце: пустое поле
    @Test
    void shouldPaymentEmptyHolder() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), null, getCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }


    //Невалидный код CVV: ввод двух цифр
    @Test
    void shouldPaymentCardInvalidCvv() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getHolder(), getInvalidCvv());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидный код CVV: ввод одной цифры
    @Test
    void shouldPaymentCardInvalidCvv2() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getHolder(), getInvalidCvv2());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидный код CVV: ввод трёх нулей
    @Test
    void shouldPaymentCardInvalidCvvZero() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getHolder(), getInvalidCvvZero());
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.wrongFormatMessage();
    }

    //Невалидный код CVV: пустое поле
    @Test
    void shouldPaymentCardInvalidEmptyCvv() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                getApprovedCardNumber(), getMonth(7), getYear(1), getHolder(), null);
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }

    //Отправка пустой формы покупки тура картой
    @Test
    void shouldPaymentEmptyAllField() {
        var mainPage = new MainPage();
        CardsInfo card = new CardsInfo(
                null, null, null, null, null);
        var paymentPage = mainPage.payment();
        paymentPage.fillForm(card);
        paymentPage.shouldFillMessage();
    }
}
