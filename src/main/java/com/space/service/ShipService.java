package com.space.service;

import com.space.model.FilterOptions;
import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    Integer getCountOfShips(FilterOptions filterOptions);
    List<Ship> getShips(FilterOptions filterOptions);
    Ship addShip(Ship ship);
    Ship findById(Long id);
    Ship updateShip(Long id, Ship srcShip);
    Boolean deleteShip(Long id);
}
