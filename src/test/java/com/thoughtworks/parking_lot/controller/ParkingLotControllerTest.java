package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;

import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingLotController.class)
public class ParkingLotControllerTest {

    private static final int PAGE = 0;
    private static final int PAGE_SIZE = 15;


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

    @Test
    void should_return_not_found_when_deleting_a_parking_lot_that_does_not_exist() throws Exception {
        ParkingLot parkingLot = createParkingLot("ParkingLot1", 50, "Pasay");

        doThrow(NotFoundException.class).when(parkingLotService).deleteParkingLot(eq("ParkingLot1"));
        ResultActions result = mvc.perform(delete("/parkingLots/ParkingLot1")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(parkingLot)));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void should_return_all_companies() throws Exception {
        ParkingLot parkingLot1= createParkingLot("ZzzParkingLot", 50, "Caloocan City");
        ParkingLot parkingLot2 = createParkingLot("AaaParkingLot", 10, "Pasay City");
        when(parkingLotService.getParkingLots(PAGE, PAGE_SIZE)).thenReturn(Arrays.asList(parkingLot2, parkingLot1));

        ResultActions result = mvc.perform(get("/parkingLots"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("AaaParkingLot"))
                .andExpect(jsonPath("$[1].name").value("ZzzParkingLot"));
    }

    @Test
    void should_return_parking_lot_detail_by_name_input() throws Exception {
        ParkingLot parkingLot1= createParkingLot("ParkingLotTest", 50, "Caloocan City");
        when(parkingLotService.getSpecificParkingLot("ParkingLotTest")).thenReturn(parkingLot1);

        ResultActions result = mvc.perform(get("/parkingLots/ParkingLotTest"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ParkingLotTest"));
    }

    @Test
    void should_return_not_found_when_parking_lot_name_does_not_exist() throws Exception {
        ParkingLot parkingLot1= createParkingLot("ParkingLotTest", 50, "Caloocan City");

        doThrow(NotFoundException.class).when(parkingLotService).getSpecificParkingLot(eq("ParkingLotTest"));
        ResultActions result = mvc.perform(get("/parkingLots/ParkingLotTest")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(parkingLot1)));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }


    @Test
    void should_update_parking_lot_capacity() throws Exception {
        ParkingLot updatedParkingLot= createParkingLot("ParkingLotTest", 50, "Caloocan City");

        when(parkingLotService.updateSpecificParkingLot(eq("ParkingLotTest"), any())).thenReturn(updatedParkingLot);
        ResultActions result = mvc.perform(put("/parkingLots/ParkingLotTest")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(updatedParkingLot)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ParkingLotTest"));
    }



    @Test
    void should_return_not_found_when_updating_parking_lot_info_name_parameter_does_not_exists() throws Exception {
        ParkingLot updatedParkingLot= createParkingLot("ParkingLotTest", 50, "Caloocan City");

        doThrow(NotFoundException.class).when(parkingLotService).updateSpecificParkingLot(eq("ParkingLotTest"), any());
        ResultActions result = mvc.perform(put("/parkingLots/ParkingLotTest")
                .contentType(APPLICATION_JSON)
                .content(mapToJson(updatedParkingLot)));

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
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
