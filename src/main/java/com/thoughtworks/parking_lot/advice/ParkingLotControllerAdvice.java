package com.thoughtworks.parking_lot.advice;

import com.thoughtworks.parking_lot.entity.CustomError;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ParkingLotControllerAdvice {

    private static final String THE_PARKING_LOT_IS_FULL = "The parking lot is full.";

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public CustomError handleNotFoundException(NotFoundException e) {
        CustomError customError = new CustomError();
        customError.setCode(404);
        customError.setMessage(e.getMessage());
        return customError;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CustomError badRequestError(HttpClientErrorException e){
        CustomError customError = new CustomError();
        customError.setCode(400);
        customError.setMessage(THE_PARKING_LOT_IS_FULL);
        return customError;
    }

}
