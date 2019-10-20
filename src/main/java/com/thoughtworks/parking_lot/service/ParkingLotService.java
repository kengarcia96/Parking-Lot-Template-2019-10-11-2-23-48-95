package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import javassist.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    private static final String RECORD_NOT_FOUND = "Record Not Found";
    private static final String RECORD_HAS_BEEN_DELETED= " Record has been deleted.";

    public ParkingLot addParkingLot(ParkingLot parkingLot) {
            parkingLotRepository.save(parkingLot);
            return parkingLot;
    }

    public String deleteParkingLot(String name) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findOnebyName(name);
        if(parkingLot != null){
            parkingLotRepository.delete(parkingLot);
            return parkingLot.getName() + RECORD_HAS_BEEN_DELETED;
        }

        throw new NotFoundException(RECORD_NOT_FOUND);
    }

    public Iterable<ParkingLot> getParkingLots(Integer page, Integer pageSize) {
        return parkingLotRepository.findAll(PageRequest.of(page, pageSize, Sort.by("name").ascending()));
    }


    public ParkingLot getSpecificParkingLot(String name) throws NotFoundException{
        ParkingLot parkingLot = parkingLotRepository.findOnebyName(name);
        if(parkingLot != null){
            return parkingLot;
        }
        throw new NotFoundException(RECORD_NOT_FOUND);
    }

    public ParkingLot updateSpecificParkingLot(String name, ParkingLot updateParkingLot) throws NotFoundException{
        ParkingLot parkingLot = parkingLotRepository.findOnebyName(name);
        if(parkingLot != null){
            parkingLot.setCapacity(updateParkingLot.getCapacity());
            parkingLotRepository.save(parkingLot);
            return parkingLot;
        }
        throw new NotFoundException(RECORD_NOT_FOUND);
    }
}
