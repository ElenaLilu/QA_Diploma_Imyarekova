package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardsInfo {
    private String number;
    private String month;
    private String year;
    private String holder;
    private String cvv;
}