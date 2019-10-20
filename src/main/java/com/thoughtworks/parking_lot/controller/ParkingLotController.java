package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotServices;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLots")
public class ParkingLotController {

    @Autowired
    ParkingLotServices parkingLotServices;


    @PostMapping(produces={"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParkingLot addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotServices.addParkingLot(parkingLot);
    }

    @DeleteMapping(path="/{name}", produces={"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteParkingLot(@PathVariable String name) throws  NotFoundException{
        return parkingLotServices.deleteParkingLot(name);
    }



}
