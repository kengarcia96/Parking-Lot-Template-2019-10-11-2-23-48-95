package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingLotController.class)
public class ParkingLotControllerTest {

    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 10;


    @MockBean
    private ParkingLotService parkingLotService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_add_a_parking_lot() throws Exception {
        ParkingLot parkingLot = createParkingLot("ParkingLot1", 50, "Pasay");

        when(parkingLotService.addParkingLot(any())).thenReturn(parkingLot);
        ResultActions result = mvc.perform(post("/parkingLots")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(parkingLot)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ParkingLot1"));
    }

    @Test
    public void should_delete_existing_company() throws Exception {
        ParkingLot parkingLot = createParkingLot("ParkingLot1", 50, "Pasay");

        when(parkingLotService.deleteParkingLot("ParkingLot1")).thenReturn("ParkingLot1 Record has been deleted.");
        ResultActions result = mvc.perform(delete("/parkingLots/ParkingLot1"));

        result.andExpect(status().isOk());
    }

    private ParkingLot createParkingLot(String name, Integer capacity, String location) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setCapacity(capacity);
        parkingLot.setLocation(location);
        return parkingLot;
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
