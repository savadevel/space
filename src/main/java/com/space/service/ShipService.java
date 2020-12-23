package com.space.service;

import com.space.model.FilterOptions;
import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    Long getCountOfShips(FilterOptions filterOptions);
    List<Ship> getShips(FilterOptions filterOptions);
}
