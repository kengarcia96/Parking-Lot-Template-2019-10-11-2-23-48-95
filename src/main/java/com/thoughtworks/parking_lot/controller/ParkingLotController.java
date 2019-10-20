package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLots")
public class ParkingLotController {

    @Autowired
    ParkingLotService parkingLotService;


    @PostMapping(produces={"application/json"})
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParkingLot addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.addParkingLot(parkingLot);
    }

    @DeleteMapping(path="/{name}", produces={"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteParkingLot(@PathVariable String name) throws  NotFoundException{
        return parkingLotService.deleteParkingLot(name);
    }

    @GetMapping(produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ParkingLot> getParkingLots(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        return parkingLotService.getParkingLots(page, pageSize);
    }

    @GetMapping(path="/{name}", produces={"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public ParkingLot getSpecificParkingLot(@PathVariable String name) throws NotFoundException{
        return parkingLotService.getSpecificParkingLot(name);
    }





}
