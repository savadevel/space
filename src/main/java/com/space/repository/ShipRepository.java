package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    @Query("select s from Ship s where " +
            "(:name is null or s.name like :name) and " +
            "(:planet is null or s.planet like :planet) and " +
            "(:shipType is null or s.shipType = :shipType) and" +
            "(:after is null or s.prodDate >= :after) and" +
            "(:before is null or s.prodDate <= :before) and" +
            "(:isUsed is null or s.isUsed = :isUsed) and" +
            "(:minSpeed is null or s.speed >= :minSpeed) and" +
            "(:maxSpeed is null or s.speed <= :maxSpeed) and" +
            "(:minCrewSize is null or s.crewSize >= :minCrewSize) and" +
            "(:maxCrewSize is null or s.crewSize <= :maxCrewSize) and" +
            "(:minRating is null or s.rating >= :minRating) and" +
            "(:maxRating is null or s.rating <= :maxRating)")
    Page<Ship> findAllByFilterOptions(
            Pageable pageable,
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") ShipType shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating);

    @Query("select count(s) from Ship s where " +
            "(:name is null or s.name like :name) and " +
            "(:planet is null or s.planet like :planet) and " +
            "(:shipType is null or s.shipType = :shipType) and" +
            "(:after is null or s.prodDate >= :after) and" +
            "(:before is null or s.prodDate <= :before) and" +
            "(:isUsed is null or s.isUsed = :isUsed) and" +
            "(:minSpeed is null or s.speed >= :minSpeed) and" +
            "(:maxSpeed is null or s.speed <= :maxSpeed) and" +
            "(:minCrewSize is null or s.crewSize >= :minCrewSize) and" +
            "(:maxCrewSize is null or s.crewSize <= :maxCrewSize) and" +
            "(:minRating is null or s.rating >= :minRating) and" +
            "(:maxRating is null or s.rating <= :maxRating)")
    Integer countAllByFilterOptions(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") ShipType shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating);
}
