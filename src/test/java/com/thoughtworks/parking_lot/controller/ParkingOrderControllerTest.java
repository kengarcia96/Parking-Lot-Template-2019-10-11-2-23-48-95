package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingOrderController.class)
public class ParkingOrderControllerTest {

        @MockBean
        private ParkingLotService parkingLotService;

        @MockBean
        private ParkingOrderService parkingOrderService;

        @Autowired
        private MockMvc mvc;

        @Test
        void should_add_a_parking_information() throws Exception {
            ParkingLot parkingLot = createParkingLot("ParkingLot1", 50, "Pasay");

            when(parkingOrderService.addParkingOrder("parkingLotA",any())).thenReturn(parkingLot);
            ResultActions result = mvc.perform(post("/parkingLots")
                    .contentType(APPLICATION_JSON)
                    .content(mapToJson(parkingLot)));

            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("ParkingLot1"));
        }

}
