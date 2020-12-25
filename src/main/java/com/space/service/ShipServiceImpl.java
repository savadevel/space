package com.space.service;

import com.space.Util;
import com.space.model.FilterOptions;
import com.space.model.Ship;
import com.space.repository.ShipRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.*;

@Service("shipService")
@Transactional(readOnly = true)
public class ShipServiceImpl implements ShipService {
    // текущий год (не забудь, что «сейчас» 3019 год);
    private static final Integer CURRENT_YEAR = 3019;

    private final ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Integer getCountOfShips(FilterOptions fo) {
        return shipRepository.countAllByFilterOptions(
                fo.getName(),
                fo.getPlanet(),
                fo.getShipType(),
                fo.getAfter(),
                fo.getBefore(),
                fo.getUsed(),
                fo.getMinSpeed(),
                fo.getMaxSpeed(),
                fo.getMinCrewSize(),
                fo.getMaxCrewSize(),
                fo.getMinRating(),
                fo.getMaxRating());
    }

    @Override
    public List<Ship> getShips(FilterOptions fo) {
        Pageable pageable = PageRequest.of(fo.getPageNumber(), fo.getPageSize(), Sort.by(fo.getOrder().getFieldName()));
        Page<Ship> notePage = shipRepository.findAllByFilterOptions(
                pageable,
                fo.getName(),
                fo.getPlanet(),
                fo.getShipType(),
                fo.getAfter(),
                fo.getBefore(),
                fo.getUsed(),
                fo.getMinSpeed(),
                fo.getMaxSpeed(),
                fo.getMinCrewSize(),
                fo.getMaxCrewSize(),
                fo.getMinRating(),
                fo.getMaxRating());

        return notePage.getContent();
    }

    @Override
    @Transactional(readOnly = false)
    public Ship addShip(Ship ship) {
        // задаем явно значения, которые имею фиксированый формат или значение по умолчанию (т.к. в гетерах Ship
        // используются расчетные поля)
        ship.setUsed(ship.getUsed());
        ship.setSpeed(ship.getSpeed());

        // При обновлении или создании корабля игнорируем параметры “id” и “rating” из тела запроса.
        ship.setId(null);
        ship.setRating(getRatingShip(ship));
        return  shipRepository.save(ship);
    }


    // Также должна присутствовать бизнес-логика:
    // Перед сохранением корабля в базу данных (при добавлении нового или при апдейте характеристик существующего),
    // должен высчитываться рейтинг корабля и сохраняться в БД. Рейтинг корабля рассчитывается по формуле:
    //
    // rating = 80 * v  * k / (y0 - y1 + 1)
    //
    // где:
    // v — скорость корабля;
    // k — коэффициент, который равен 1 для нового корабля и 0,5 для использованного;
    // y0 — текущий год (не забудь, что «сейчас» 3019 год);
    // y1 — год выпуска корабля.
    private Double getRatingShip(Ship ship) {

        if (ship.getProdDate() == null || ship.getSpeed() == null || ship.getUsed() == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(ship.getProdDate());

        // Рейтинг корабля. Используй математическое округление до сотых.
        return Util.roundToHundredths(
                80d * ship.getSpeed() * (ship.getUsed() ? 0.5d : 1d)
                /
                (CURRENT_YEAR - calendar.get(Calendar.YEAR) + 1));
    }

    @Override
    public Ship findById(Long id) {
        // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean deleteShip(Long id) {
        // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
        if (!shipRepository.existsById(id))
            return false;

        shipRepository.deleteById(id);

        return true;
    }

    @Override
    @Transactional(readOnly = false)
    public Ship updateShip(Long id, Ship srcShip) {

        Ship destShip = findById(id);

        if (destShip == null) {
            // Если корабль не найден в БД, необходимо ответить ошибкой с кодом 404.
            return null;
        }
        // “name”:[String], --optional
        // “planet”:[String], --optional
        // “shipType”:[ShipType], --optional
        // “prodDate”:[Long], --optional
        // “isUsed”:[Boolean], --optional
        // “speed”:[Double], --optional
        // “crewSize”:[Integer] --optional

        // При запросе POST /rest/ships/{id} с пустым телом запроса, корабль не должен изменяться
        if (getPropertyNames(srcShip, false).length == 0) {
            return destShip;
        }

        // При обновлении или создании корабля игнорируем параметры “id” и “rating” из тела запроса.
        srcShip.setId(null);

        // При запросе POST /rest/ships/{id} с rating в теле запроса, должны быть обновлены поля, кроме поля rating
        srcShip.setRating(null);

        // Обновлять нужно только те поля, которые не null.
        BeanUtils.copyProperties(srcShip, destShip, getPropertyNames(srcShip, true));

        destShip.setRating(getRatingShip(destShip));

        return shipRepository.save(destShip);
    }

    // возвращает список атрибутов корабля
    // при isNull == true - только атрибуты имеющие null значения
    // при isNull != true - только атрибуты не имеющие null значения
    private String[] getPropertyNames(Ship ship, boolean isNull) {
        final BeanWrapper src = new BeanWrapperImpl(ship);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> names = new HashSet<String>();

        for(PropertyDescriptor pd : pds) {
            if ("class".equals(pd.getName()) || "used".equals(pd.getName()))
                // пропускаем системные атрибуты
                continue;
            if (isNull && src.getPropertyValue(pd.getName()) == null)
                names.add(pd.getName());
            else if (!isNull && src.getPropertyValue(pd.getName()) != null)
                names.add(pd.getName());
        }

        return names.toArray(new String[0]);
    }


}
