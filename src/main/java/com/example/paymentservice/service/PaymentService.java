package com.example.paymentservice.service;

import com.example.paymentservice.models.PaymentEntity;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    public PaymentEntity capturePayment(String id) throws RazorpayException;
}
