package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.CardsInfo;
import ru.netology.data.DbUtils;
import ru.netology.page.MainPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;

@Feature("Тестируем покупку тура в кредит")
public class CreditTest {

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
        static void tearDownAll() throws SQLException {
            SelenideLogger.removeListener("allure");
            DbUtils.deleteTables();
        }

        //Проверка карты со статусом APPROVED
            @Test
            void shouldCreditWithApprovedCard() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.successMessage();
            String actual = DbUtils.getStatusPayment();
            assertEquals("APPROVED", actual);
        }

        //Проверка карты со статусом DECLINED
        @Test
        void shouldPaymentWithDeclinedCard() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getDeclinedCardNumber(), getMonth(0), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.failMessage();
            String actual = DbUtils.getStatusPayment();
            assertEquals("DECLINED", actual);
        }

        //Проверка карты с несуществующим номером
        @Test
        void shouldPaymentWithInvalidCardNumber() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getInvalidCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.failMessage();
        }

        //Проверка карты, состоящей из 12 цифр
        @Test
        void shouldPaymentWithInvalidCardNumberShort() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getInvalidShortCardNumber(), getMonth(10), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongFormatMessage();
        }

        //Проверка карты, номер которой состоит из букв
        @Test
        void shouldPaymentWithInvalidCardNumberOfLetters() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getCardNumberWithLetters(), getMonth(10), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongFormatMessage();
        }

        //Проверка поля карты с пустым значением
        @Test
        void shouldPaymentEmptyField() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    null, getMonth(10), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }

        //Оплата картой, срок окончания которой - год, предшесвующий текущему
        @Test
        void shouldPaymentExpiredCard() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(0), getYear(-1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.cardExpiredMessage();
        }

        //Оплата картой, срок окончания которой более, чем через 5 лет относительно текущего года
        @Test
        void shouldPaymentCardExpirationPlusFiveYears() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(0), getYear(6), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Оплата картой, где срок окончания указан буквами
        @Test
        void shouldPaymentCardYearIndicatedLetters() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getInvalidYear(), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Оплата картой, где год срока окончания не указан
        @Test
        void shouldPaymentCardWithEmptyYear() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), null, getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }

        //Оплата картой, у которой месяц превышает 12
        @Test
        void shouldPaymentIncorrectCardExpirationDate() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getInvalidMonthInvalidPeriod(), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Оплата картой, месяц которой указан: 00
        @Test
        void shouldPaymentCardInvalidMonth() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getInvalidMonth(), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Оплата картой, месяц которой указан буквами
        @Test
        void shouldPaymentCardMonthWithLetters() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getInvalidMonth2(), getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Оплата картой, где поле месяца не заполнено
        @Test
        void shouldPaymentEmptyFieldMonth() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), null, getYear(1), getHolder(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }


        //Невалидные данные о владельце: имя и фамилия на кириллице
        @Test
        void shouldPaymentInvalidHolderCard() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardCyrillic(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидные данные о владельце: цифры в имени
        @Test
        void shouldPaymentInvalidHolderCardWithNumbers() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardWithNumbers(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидные данные о владельце: введена только одна буква
        @Test
        void shouldPaymentInvalidHolderCardWithOneLetter() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getInvalidHolderCardOneLetterName(), getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидные данные о владельце: пустое поле
        @Test
        void shouldPaymentEmptyHolder() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), null, getCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }


        //Невалидный код CVV: ввод двух цифр
        @Test
        void shouldPaymentCardInvalidCvv() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvv());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидный код CVV: ввод одной цифры
        @Test
        void shouldPaymentCardInvalidCvv2() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvv2());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидный код CVV: ввод трёх нулей
        @Test
        void shouldPaymentCardInvalidCvvZero() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), getInvalidCvvZero());
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.wrongTermMessage();
        }

        //Невалидный код CVV: пустое поле
        @Test
        void shouldPaymentCardInvalidEmptyCvv() throws SQLException {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    getApprovedCardNumber(), getMonth(10), getYear(1), getHolder(), null);
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }

        //Отправка пустой формы покупки тура картой
        @Test
        void shouldPaymentEmptyAllField() {
            var mainPage = new MainPage();
            CardsInfo card = new CardsInfo(
                    null, null, null, null, null);
            var creditPage = mainPage.paymentOnCredit();
            creditPage.fillForm(card);
            creditPage.shouldFillMessage();
        }
}
