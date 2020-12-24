package com.space.controller;

import org.springframework.stereotype.Component;

// Не валидным считается id, если он:
//- не числовой
//- не целое число
//- не положительный
@Component
public class IdShipValidator  {

    // проверка запроса на создание корабля
    public boolean validate(String id) {
        //- не числовой
        //- не целое число
        //- не положительный
        if (id == null || !id.matches("\\d+") || "0".equals(id))
            return false;

        return true;
    }
}
