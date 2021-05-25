package com.space.controller;

import com.space.model.FilterOptions;
import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rest")
public class ShipController {
    private final ShipValidator shipValidator;
    private final UpdateShipValidator updateShipValidator;
    private final IdShipValidator idShipValidator;
    private final ShipService shipService;

    public ShipController(
            ShipService shipService,
            ShipValidator shipValidator,
            UpdateShipValidator updateShipValidator,
            IdShipValidator idShipValidator) {
        this.shipService = shipService;
        this.shipValidator = shipValidator;
        this.updateShipValidator = updateShipValidator;
        this.idShipValidator = idShipValidator;
    }

    // получать список всех существующих кораблей
    // получать отфильтрованный список кораблей в соответствии с переданными фильтрами
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ships")
    public List<Ship> onListShipsByFilter(@ModelAttribute FilterOptions filterOptions) {
        return shipService.getShips(filterOptions);
    }

    // получать количество кораблей, которые соответствуют фильтрам.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ships/count")
    public Integer onCountShipsByFilter(@ModelAttribute FilterOptions filterOptions) {
        return shipService.getCountOfShips(filterOptions);
    }

    // создавать новый корабль
    //
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
    @PostMapping("/ships")
    public ResponseEntity<Ship> onCreateShip(@RequestBody Ship ship, BindingResult result) {

        shipValidator.validate(ship, result);

        if (result.hasErrors()) {
            // Мы не можем создать корабль, если:
            // ...
            // В случае всего вышеперечисленного необходимо ответить ошибкой с кодом 400.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        shipService.addShip(ship);

        return ResponseEntity.ok(ship);
    }

    // получать корабль по id;
    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> onFindShipById(@PathVariable String id) {

        if (!idShipValidator.validate(id)) {
            // Если значение id не валидное, необходимо ответить ошибкой с кодом 400.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Ship ship = shipService.findById(Long.valueOf(id));

        // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
        return ship == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(ship);
    }

    // удалять корабль;
    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Void> onDeleteShipById(@PathVariable String id) {

        if (!idShipValidator.validate(id)) {
            // Если значение id не валидное, необходимо ответить ошибкой с кодом 400.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
        return shipService.deleteShip(Long.valueOf(id)) ?
                ResponseEntity.ok().body(null) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    // редактировать характеристики существующего корабля;
    @PostMapping("/ships/{id}")
    public ResponseEntity<Ship> onUpdateShip(@PathVariable String id, @RequestBody Ship ship, BindingResult result) {

        updateShipValidator.validate(ship, result);

        if (result.hasErrors() || !idShipValidator.validate(id)) {
            // Мы не можем обновить корабль, если:
            // ...
            // В случае всего вышеперечисленного необходимо ответить ошибкой с кодом 400.
            // или
            // Если значение id не валидное, необходимо ответить ошибкой с кодом 400.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Ship updated = shipService.updateShip(Long.valueOf(id), ship);

        // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
        return updated == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(updated);
    }
}
