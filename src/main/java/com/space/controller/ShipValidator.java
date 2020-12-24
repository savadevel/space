package com.space.controller;

import com.space.model.Ship;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Calendar;

// проверка запроса на создание корабля
//
// мы не можем создать корабль, если:
//- указаны не все параметры из Data Params (кроме isUsed);
//- длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);
//- значение параметра “name” или “planet” пустая строка;
//- скорость или размер команды находятся вне заданных пределов;
//- “prodDate”:[Long] < 0;
//- год производства находятся вне заданных пределов.
//В случае всего вышеперечисленного необходимо ответить ошибкой с кодом 400.
@Component
public class ShipValidator implements Validator {

    // проверка на возможность валидации переданного класса объекта
    @Override
    public boolean supports(Class<?> clazz) {
        return Ship.class.equals(clazz);
    }

    // проверка запроса на создание корабля
    @Override
    public void validate(Object target, Errors errors) {
        Ship ship = (Ship) target;

        // указаны не все параметры из Data Params (кроме isUsed)
        validateEmptyOrWhitespace(ship, errors);
        validateStringMaxLength(ship, errors);
        validateRange(ship, errors);
    }

    // скорость или размер команды находятся вне заданных пределов;
    // “prodDate”:[Long] < 0;
    // год производства находятся вне заданных пределов.
    private void validateRange(Ship ship, Errors errors) {
        if (errors.hasErrors())
            return;

        // Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно. Используй математическое округление
        // до сотых.
        if (ship.getSpeed() < 0.01d || ship.getSpeed() > 0.99d){
            errors.rejectValue("speed", "Range");
        }

        // Количество членов экипажа. Диапазон значений 1..9999 включительно.
        if (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999){
            errors.rejectValue("crewSize", "Range");
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(ship.getProdDate());

        // Параметры даты между фронтом и сервером передаются в миллисекундах (тип Long) начиная с 01.01.1970.
        // Диапазон значений года 2800..3019 включительно
        if (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019) {
            errors.rejectValue("prodDate", "Range");
        }
    }

    // длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);
    private void validateStringMaxLength(Ship ship, Errors errors) {
        if (errors.hasErrors())
            return;

        if (ship.getName().length() > 50) {
            errors.rejectValue("name", "MaxLength");
        }
        if (ship.getPlanet().length() > 50) {
            errors.rejectValue("planet", "MaxLength");
        }
    }

    // указаны не все параметры из Data Params (кроме isUsed);
    // значение параметра “name” или “planet” пустая строка;
    // Data Params
    // {
    //“name”:[String],
    //“planet”:[String],
    //“shipType”:[ShipType],
    //“prodDate”:[Long],
    //“isUsed”:[Boolean], --optional, default=false
    //“speed”:[Double],
    //“crewSize”:[Integer]
    //}
    private void validateEmptyOrWhitespace(Ship ship, Errors errors) {

        if (errors.hasErrors())
            return;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "planet", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "shipType", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "prodDate", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "speed", "NotEmpty");
        ValidationUtils.rejectIfEmpty(errors, "crewSize", "NotEmpty");

        // Если в запросе на создание корабля нет параметра “isUsed”, то считаем, что пришло значение “false”.
        // см. геттер Ship.getUsed - если значение null, то возвращает false
    }
}
