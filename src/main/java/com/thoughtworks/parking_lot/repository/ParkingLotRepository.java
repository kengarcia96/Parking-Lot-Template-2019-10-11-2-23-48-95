package com.thoughtworks.parking_lot.repository;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    @Query("Select p from ParkingLot p where p.name = :name")
    ParkingLot findOnebyName(@Param("name") String name);
}
