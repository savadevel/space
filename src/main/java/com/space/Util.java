package com.space;

public class Util {
    // округляет до сотых, если значение null, то возвращает null
    public static Double roundToHundredths(Double value) {
        return value == null ? null : Math.round(value * 100d) / 100d;
    }
}
