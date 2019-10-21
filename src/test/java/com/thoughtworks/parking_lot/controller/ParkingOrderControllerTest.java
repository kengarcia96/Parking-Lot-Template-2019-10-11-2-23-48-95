package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.entity.ParkingLot;
import com.thoughtworks.parking_lot.entity.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import javassist.NotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingOrderController.class)
public class ParkingOrderControllerTest {


        private static final String CLOSED = "Closed";

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

        @Test
        void should_update_parking_order_information() throws Exception{

            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setName("ParkingLot1");
            parkingLot.setCapacity(50);
            parkingLot.setLocation("Pasay");


            ParkingOrder parkingOrder = createParkingOrder();

            when(parkingLotService.getSpecificParkingLot("ParkingLot1")).thenReturn(parkingLot);
            parkingOrder.setOrderStatus(CLOSED);
            parkingOrder.setCloseTime(Timestamp.valueOf("2019-10-21 01:00:00.0"));


            when(parkingOrderService.updateParkingOrder("1")).thenReturn(parkingOrder);
            ResultActions result = mvc.perform(patch("/parkingLots/ParkingLot1/orders/1", createParkingOrder())
                    .contentType(APPLICATION_JSON)
                    .content(mapToJson(parkingOrder)));

            result.andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void should_return_error_when_parking_is_full() throws Exception {
            ParkingLot parkingLot = new ParkingLot();
            when(parkingLot.getName()).thenReturn("ParkingLot1");

            ParkingOrder parkingOrder = createParkingOrder();

            ResultActions result = mvc.perform(post("/parkingLots/ParkingLot1/orders")
                    .contentType(APPLICATION_JSON)
                    .content(mapToJson(parkingOrder)));

            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(400));
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
