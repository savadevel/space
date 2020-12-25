package com.space.controller;

import com.space.model.Ship;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;

// проверка запроса на обновление корабля
//
// мы не можем обновить корабль, если:
//- длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);
//- скорость или размер команды находятся вне заданных пределов;
//- “prodDate”:[Long] < 0;
//- год производства находятся вне заданных пределов.
//В случае всего вышеперечисленного необходимо ответить ошибкой с кодом 400.
@Component
public class UpdateShipValidator implements Validator {

    // проверка на возможность валидации переданного класса объекта
    @Override
    public boolean supports(Class<?> clazz) {
        return Ship.class.equals(clazz);
    }

    // проверка запроса на создание корабля
    @Override
    public void validate(Object target, Errors errors) {
        Ship ship = (Ship) target;

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
        if (ship.getSpeed() != null && (ship.getSpeed() < 0.01d || ship.getSpeed() > 0.99d)) {
            errors.rejectValue("speed", "Range");
        }

        // Количество членов экипажа. Диапазон значений 1..9999 включительно.
        if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)) {
            errors.rejectValue("crewSize", "Range");
        }

        if (ship.getProdDate() == null)
            return;

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

        if (ship.getName() != null && (ship.getName().isEmpty() || ship.getName().length() > 50)) {
            errors.rejectValue("name", "MaxLength");
        }
        if (ship.getPlanet() != null && (ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50)) {
            errors.rejectValue("planet", "MaxLength");
        }
    }
}
