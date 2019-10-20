package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.entity.Order;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "parkingLots/{parkingLotName}/orders")
public class ParkingOrderController {

    @Autowired
    private ParkingOrderService parkingOrderService;

    @PostMapping(produces={"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Order addParkingOrder(@PathVariable("parkingLotName") String parkingLotName, String plateNumber){
        return parkingOrderService.addParkingOrder(parkingLotName, plateNumber);
    }

}
