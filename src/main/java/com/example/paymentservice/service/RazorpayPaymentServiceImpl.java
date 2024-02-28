package com.example.paymentservice.service;

import com.example.paymentservice.models.PaymentMode;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentServiceImpl implements PaymentService {
    PaymentRepository paymentRepository;
    public RazorpayPaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository=paymentRepository;
    }
    public String initiatePayment(long orderId,long userId,long amount) throws RazorpayException {
        RazorpayClient razorpay=new RazorpayClient("rzp_test_pE1o9lqflePU4O","xbMkcJ7m7jHheRhvIT9SCqGA");
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        //paymentLinkRequest.put("expire_by",1691097057);
        //paymentLinkRequest.put("orderId",orderId);
        /*JSONObject customer = new JSONObject();
        customer.put("customer_id",userId);
        paymentLinkRequest.put("customer",customer);*/
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        paymentLinkRequest.put("callback_url","https://example-callback-url.com/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
        return payment.toString();
    }

    @Override
    public void updatePayment(long orderId, String chargeId,PaymentStatus paymentStatus, PaymentMode paymentMode) {

    }

    @Override
    public void reconcilePayments() throws RazorpayException {

    }
}
