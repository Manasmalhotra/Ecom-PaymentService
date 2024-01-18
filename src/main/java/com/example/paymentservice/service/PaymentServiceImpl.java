package com.example.paymentservice.service;

import com.example.paymentservice.models.PaymentEntity;
import com.example.paymentservice.models.PaymentMode;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class PaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository=paymentRepository;
    }
    public PaymentEntity capturePayment(String id) throws RazorpayException {
        RazorpayClient razorpay=new RazorpayClient("rzp_test_pE1o9lqflePU4O","xbMkcJ7m7jHheRhvIT9SCqGA");
        Payment payment = razorpay.payments.fetch(id);
        PaymentEntity paymentToBeStored= PaymentEntity.builder().
                id(payment.get("id"))
                .amount(payment.get("amount"))
                .currency(payment.get("currency"))
                .paymentMode(PaymentMode.valueOf(payment.get("method")))
                .paymentStatus(PaymentStatus.valueOf(payment.get("status"))).build();
        return paymentRepository.save(paymentToBeStored);
    }
}
