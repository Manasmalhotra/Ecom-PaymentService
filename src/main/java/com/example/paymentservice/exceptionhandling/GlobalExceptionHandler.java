package com.example.paymentservice.exceptionhandling;

import com.razorpay.RazorpayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(RazorpayException.class)
    public ResponseEntity<ErrorDetails> ResourceNotFoundExceptionHandler(RazorpayException e, WebRequest w){
        ErrorDetails error=new ErrorDetails(new Date(),e.getMessage(),w.getDescription(false));
       return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
