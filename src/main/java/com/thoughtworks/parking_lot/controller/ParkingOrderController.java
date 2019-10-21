package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.entity.ParkingOrder;
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
    public ParkingOrder addParkingOrder(@PathVariable("parkingLotName") String parkingLotName, @RequestBody ParkingOrder plateNumber){
        return parkingOrderService.addParkingOrder(parkingLotName, plateNumber);
    }

}

