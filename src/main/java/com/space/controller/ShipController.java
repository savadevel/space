package com.space.controller;

import com.space.model.FilterOptions;
import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    // получать список всех существующих кораблей;
    @GetMapping("/rest/ships")
    @ResponseBody
    public List<Ship> onShips(@ModelAttribute FilterOptions filterOptions) {
        return shipService.getShips(filterOptions);
    }

    // получать количество кораблей, которые соответствуют фильтрам.
    @GetMapping("/rest/ships/count")
    @ResponseBody
    public Long onShipsCount(@ModelAttribute FilterOptions filterOptions

/*            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false) ShipOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize*/
    ) {
        return shipService.getCountOfShips(filterOptions);
    }


}
