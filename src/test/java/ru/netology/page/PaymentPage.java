package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.CardsInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    private SelenideElement headingPayment = $$("h3.heading").find(exactText("Оплата по карте"));
    private final ElementsCollection fields = $$(".input__control");
    private final SelenideElement numberField = $("[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("[placeholder='08']");
    private final SelenideElement yearField = $("[placeholder='22']");
    private final SelenideElement holderField = fields.get(3);
    private final SelenideElement cvvField = $("[placeholder='999']");

    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement successNotification = $(byText("Операция одобрена Банком."));
    private final SelenideElement failNotification = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement wrongFormat = $(byText("Неверный формат"));
    private final SelenideElement wrongTerm = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement cardExpired = $(byText("Истёк срок действия карты"));
    private final SelenideElement requiredField = $(byText("Поле обязательно для заполнения"));

    public PaymentPage() {
        headingPayment.shouldBe(visible);
    }
    public void fillForm(CardsInfo info) {
        numberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        holderField.setValue(info.getHolder());
        cvvField.setValue(info.getCvv());
        continueButton.click();
    }

    public void successMessage() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void failMessage() {
        failNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void wrongFormatMessage() {
        wrongFormat.shouldBe(visible);
    }

    public void wrongTermMessage() {
        wrongTerm.shouldBe(visible);
    }

    public void cardExpiredMessage() {
        cardExpired.shouldBe(visible);
    }

    public void shouldFillMessage() {
        requiredField.shouldBe(visible);
    }

}