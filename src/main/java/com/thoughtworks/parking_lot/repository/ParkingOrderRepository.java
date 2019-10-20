package com.thoughtworks.parking_lot.repository;

import com.thoughtworks.parking_lot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingOrderRepository extends JpaRepository<Order, Long> {
}
