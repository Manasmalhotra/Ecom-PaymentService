package com.example.paymentservice.controller;

import com.example.paymentservice.dto.InitiateRequestDTO;
import com.example.paymentservice.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }
    @PostMapping
    public ResponseEntity<String> capturePayment (@RequestBody InitiateRequestDTO requestDTO) throws RazorpayException, StripeException {
        MultiValueMapAdapter<String,Long>headers=new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE,requestDTO.getOrderId());
        return ResponseEntity.ok(paymentService.initiatePayment(requestDTO.getOrderId(),requestDTO.getUserId(),requestDTO.getAmount()));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void reconcilePayments() throws StripeException, RazorpayException {
        paymentService.reconcilePayments();
    }
}
