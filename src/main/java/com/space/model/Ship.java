package com.space.model;


import javax.persistence.*;
import java.sql.Date;

// В проекте должна использоваться сущность Ship
@Entity
@Table(name = "ship")
public class Ship {
    // ID корабля
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Название корабля (до 50 знаков включительно)
    private String name;
    // Планета пребывания (до 50 знаков включительно)
    private String planet;
    // Тип корабля
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    // Дата выпуска. Диапазон значений года 2800..3019 включительно
    private Date prodDate;
    // Использованный / новый
    private Boolean isUsed;
    // Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно.
    // Используй математическое округление до сотых.
    private Double speed;
    // Количество членов экипажа. Диапазон значений 1..9999 включительно.
    private Integer crewSize;
    //  Рейтинг корабля. Используй математическое округление до сотых.
    private Double rating;

    public Ship() {
    }


    public Ship(String name, String planet,
                ShipType shipType, Date prodDate,
                Boolean isUsed, Double speed, Integer crewSize,
                Double rating) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
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

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "\nShip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
