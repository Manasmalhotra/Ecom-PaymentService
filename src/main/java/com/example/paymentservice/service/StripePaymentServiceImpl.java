package com.example.paymentservice.service;

import com.example.paymentservice.models.OrderStatus;
import com.example.paymentservice.models.PaymentEntity;
import com.example.paymentservice.models.PaymentMode;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.repository.PaymentRepository;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Primary
@Service
public class StripePaymentServiceImpl implements PaymentService{


    @Value(value = "${stripe.key}")
    String stripeApikey;
    private final PaymentRepository paymentRepository;
    OrderService orderService;

    public StripePaymentServiceImpl(PaymentRepository paymentRepository,OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService=orderService;
    }

    @Override
    public String initiatePayment(long orderId, long userId, long amount) throws RazorpayException, StripeException {
        Stripe.apiKey=stripeApikey;
        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("inr")
                        .setUnitAmount(amount)
                        .setProductData(
                                PriceCreateParams.ProductData.builder().setName("iPhone 15").build()
                        )
                        .build();
        Price price = Price.create(priceCreateParams);

        RequestOptions options =
                RequestOptions.builder()
                        .setIdempotencyKey(String.valueOf(orderId))
                        .build();

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        ).putMetadata("orderID",String.valueOf(orderId))
                        .setInactiveMessage("Sorry, we are out of stock for now!")
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params,options);
        PaymentEntity payment=PaymentEntity.builder().paymentStatus(PaymentStatus.CREATED)
                .id(paymentLink.getId())
                .orderId(orderId)
                .currency("inr")
                .amount(amount).build();
        paymentRepository.save(payment);
        return paymentLink.toString();
    }

    @Override
    public void updatePayment(long orderId, String chargeId,PaymentStatus paymentStatus, PaymentMode paymentMode) {
             PaymentEntity payment=paymentRepository.findByOrderId(orderId);
             payment.setPaymentMode(paymentMode);
             payment.setPaymentStatus(paymentStatus);
             payment.setPaymentMode(paymentMode);
             payment.setChargeId(chargeId);
             paymentRepository.save(payment);
    }



    @Override
    public void reconcilePayments() throws StripeException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);

        //Get payments created an hour before for which status is still not updated to success or failure.
       List<PaymentEntity>pendingPayments=paymentRepository.findAllByPaymentStatusAndCreatedOnBetween(PaymentStatus.CREATED,oneHourAgo, now);

       for(PaymentEntity payment: pendingPayments){
           Charge charge=Charge.retrieve(payment.getChargeId());
           if(payment.getPaymentStatus().equals(PaymentStatus.CREATED)){ //Still payment is in created status and not confirmed
               if(charge.getStatus()=="succeeded"){
                   payment.setPaymentStatus(PaymentStatus.CAPTURED);
                   orderService.updateOrderStatus(payment.getOrderId(), OrderStatus.PACKAGING);
               }
               else{
                   payment.setPaymentStatus(PaymentStatus.FAILED);
                   orderService.updateOrderStatus(payment.getOrderId(), OrderStatus.PAYMENT_FAILED);
               }
           }
       }
    }
}
