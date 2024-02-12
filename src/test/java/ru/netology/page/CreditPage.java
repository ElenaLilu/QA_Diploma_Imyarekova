package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.CardsInfo;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
public class CreditPage {
    private SelenideElement numberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement holderField = $$("[class='input__control']").get(3);
    private SelenideElement cvvField = $("[placeholder='999']");
    private SelenideElement continueButton = $(byText("Продолжить"));

    private SelenideElement successNotification = $(withText("Операция одобрена Банком."));
    private SelenideElement failNotification = $(withText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement wrongFormat = $(withText("Неверный формат"));
    private SelenideElement wrongTerm = $(withText("Неверно указан срок действия карты"));
    private SelenideElement cardExpired = $(withText("Истёк срок действия карты"));
    private SelenideElement requiredField = $(withText("Поле обязательно для заполнения"));

    public void fillForm(CardsInfo info) {
        numberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        holderField.setValue(info.getHolder());
        cvvField.setValue(info.getCvv());
        continueButton.click();
    }

    public void successMessage() {
        successNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void failMessage() {
        failNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void wrongFormatMessage() {
        wrongFormat.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void wrongTermMessage() {
        wrongTerm.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void cardExpiredMessage() {
        cardExpired.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldFillMessage() {
        requiredField.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
