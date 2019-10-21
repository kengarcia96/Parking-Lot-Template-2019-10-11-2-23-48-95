package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class ParkingOrderService {

    private static final String OPEN = "Open";
    private static final String CLOSED = "Closed";
    private static final String THE_PARKING_LOT_IS_FULL = "The parking lot is full.";

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public ParkingOrder addParkingOrder(String parkingLotName, ParkingOrder plateNumber) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findByName(parkingLotName);
        if (parkingLot.getCapacity() == 0) {
            throw new NotFoundException(THE_PARKING_LOT_IS_FULL);
        }
        updateParkingLotCapacity(parkingLot);
        return parkingOrderRepository.save(orderInfo(parkingLotName, plateNumber));
    }

    private ParkingOrder orderInfo(String parkingLotName, ParkingOrder plateNumber) {
        ParkingOrder order = new ParkingOrder();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setParkingLotName(parkingLotName);
        order.setPlateNumber(plateNumber.getPlateNumber());
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

    public ParkingOrder updateParkingOrder(String orderNumber) {

        ParkingOrder searchParkingOrder = parkingOrderRepository.findByOrderNumber(orderNumber);

        if(searchParkingOrder != null){
            searchParkingOrder.setOrderStatus(CLOSED);
            searchParkingOrder.setCloseTime(new Timestamp(System.currentTimeMillis()));
            parkingOrderRepository.save(searchParkingOrder);
        }

        return searchParkingOrder;
    }
}
