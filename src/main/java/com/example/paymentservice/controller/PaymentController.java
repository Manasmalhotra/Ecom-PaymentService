package com.example.paymentservice.controller;

import com.example.paymentservice.models.PaymentEntity;
import com.example.paymentservice.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> capturePayment (@PathVariable String id) throws RazorpayException {
        return ResponseEntity.ok(paymentService.capturePayment(id));
    }
}
