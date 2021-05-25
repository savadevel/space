package com.space.model;

import com.space.controller.ShipOrder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;


public class FilterOptions {
    private String name;
    private String planet;
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    private Long after;
    private Long before;
    private Double minSpeed;
    private Double maxSpeed;
    private Integer minCrewSize;
    private Integer maxCrewSize;
    private Double minRating;
    private Double maxRating;
    private ShipOrder order;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isUsed;

    public Boolean getIsUsed() {
        return isUsed;
    }

    // Если использовать только setUsed, то ModelAttribute не задает поле Boolean из запроса в объекте FilterOptions
    // т.к. пытается найти "isUsed" (из запроса) по названию метода "setUsed", т.е. "used"
    public void setIsUsed(Boolean used) {
        isUsed = used;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Date getAfter() {
        return after == null ? null : new Date(after);
    }

    public void setAfter(Long after) {
        this.after = after;
    }

    public Date getBefore() {
        return before == null ? null : new Date(before);
    }

    public void setBefore(Long before) {
        this.before = before;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMinCrewSize() {
        return minCrewSize;
    }

    public void setMinCrewSize(Integer minCrewSize) {
        this.minCrewSize = minCrewSize;
    }

    public Integer getMaxCrewSize() {
        return maxCrewSize;
    }

    public void setMaxCrewSize(Integer maxCrewSize) {
        this.maxCrewSize = maxCrewSize;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Double maxRating) {
        this.maxRating = maxRating;
    }

    public ShipOrder getOrder() {
        return order == null ? ShipOrder.ID : order;
    }

    public void setOrder(ShipOrder order) {
        this.order = order;
    }

    // Если параметр pageNumber не указан – нужно использовать значение 0.
    public Integer getPageNumber() {
        return pageNumber == null ? 0 : pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    // Если параметр pageSize не указан – нужно использовать значение 3.
    public Integer getPageSize() {
        return pageSize == null ? 3 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    // Поиск по полям name и planet происходить по частичному соответствию. Например, если в БД есть корабль с именем
    // «Левиафан», а параметр name задан как «иа» - такой корабль должен отображаться в результатах (Левиафан).
    public String getName() {
        return name == null ? null : "%" + name + "%";
    }

    public void setName(String name) {
        this.name = name;
    }

    // Поиск по полям name и planet происходить по частичному соответствию. Например, если в БД есть корабль с именем
    // «Левиафан», а параметр name задан как «иа» - такой корабль должен отображаться в результатах (Левиафан).
    public String getPlanet() {
        return planet == null ? null : "%" + planet + "%";
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }
}
