package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
            ParkingOrder parkingOrder = createParkingOrder();

            when(parkingOrderService.addParkingOrder(eq("parkinglotA"),any())).thenReturn(parkingOrder);
            ResultActions result = mvc.perform(post("/parkingLots/parkinglotA/orders")
                    .contentType(APPLICATION_JSON)
                    .content(mapToJson(parkingOrder)));

            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.plateNumber").value("TXT1234"));
        }

        private ParkingOrder createParkingOrder() {
            ParkingOrder parkingOrder = new ParkingOrder();
            parkingOrder.setOrderNumber("1");
            parkingOrder.setPlateNumber("TXT1234");
            parkingOrder.setCreationTime(new Timestamp(System.currentTimeMillis()));
            parkingOrder.setCloseTime(new Timestamp(System.currentTimeMillis()));
            parkingOrder.setOrderStatus("Open");

            return parkingOrder;
        }

        public String mapToJson(Object obj) throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(obj);
        }

}
