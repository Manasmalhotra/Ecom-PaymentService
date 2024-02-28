package com.example.paymentservice.service;

import com.example.paymentservice.models.PaymentMode;
import com.example.paymentservice.models.PaymentStatus;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;


public interface PaymentService {
    String initiatePayment(long orderId,long userId,long amount) throws RazorpayException, StripeException;

    void updatePayment(long orderId, String chargeId,PaymentStatus paymentStatus, PaymentMode paymentMode);

    void reconcilePayments() throws StripeException, RazorpayException;
}
