package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker();
    public static String getApprovedCardNumber() {
        return ("4444 4444 4444 4441");
    }

    public static String getDeclinedCardNumber() {
        return ("4444 4444 4444 4442");
    }

    public static String getInvalidCardNumber() {
        return faker.finance().creditCard();
    }

    public static String getInvalidShortCardNumber() {
        return faker.numerify("#### #### #### ##");
    }

    public static String getCardNumberWithLetters() {
        return ("аааа аааа аааа аааа");
    }

    public static String getMonth(int plusMonth) {
        return LocalDate.now().plusMonths(plusMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getInvalidMonth2() {
        return "аб";
    }

    public static String getInvalidMonthInvalidPeriod() {
        return "15";
    }

    public static String getInvalidMonth() {
        return "00";
    }

    public static String getYear(int plusYears) {
        return LocalDate.now().plusYears(plusYears).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidYear() {
        return "аб";
    }

    public static String getCvv() {
        return faker.numerify("###");
    }

    public static String getInvalidCvv() {
        return faker.numerify("##");
    }

    public static String getInvalidCvv2() {
        return faker.numerify("#");
    }

    public static String getInvalidCvvZero() {
        return "000";
    }

    public static String getHolder() {
        return faker.name().name();
    }

    public static String getInvalidHolderCardCyrillic() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().name();
    }

    public static String getInvalidHolderCardWithNumbers() {
        return (faker.name().firstName() + faker.numerify("#####"));
    }
    public static String getInvalidHolderCardOneLetterName() {
        return "I";
    }

}
