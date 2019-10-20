package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.Order;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class ParkingOrderService {

    private static final String OPEN = "Open";

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;


    public Order addParkingOrder(String parkingLotName, String plateNumber) {
        ParkingLot parkingLot = parkingLotRepository.findOnebyName(parkingLotName);
        if (parkingLot.getCapacity() == 0) {
            return null;
        }
        updateParkingLotCapacity(parkingLot);
        return parkingOrderRepository.save(orderInfo(parkingLotName, plateNumber));
    }

    private Order orderInfo(String parkingLotName, String plateNumber) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setParkingLotName(parkingLotName);
        order.setPlateNumber(plateNumber);
        order.setCreationTime(new Timestamp(System.currentTimeMillis()));
        order.setOrderStatus(OPEN);
        order.setCloseTime(null);

        return order;
    }

    private void updateParkingLotCapacity(ParkingLot parkingLot) {
        int capacity = parkingLot.getCapacity() - 1;
        parkingLot.setCapacity(capacity);
        parkingLotRepository.save(parkingLot);
    }

}
