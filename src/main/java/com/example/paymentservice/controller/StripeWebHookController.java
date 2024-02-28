package com.example.paymentservice.controller;

import com.example.paymentservice.models.OrderStatus;
import com.example.paymentservice.models.PaymentMode;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.service.OrderService;
import com.example.paymentservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripeWebHook")
public class StripeWebHookController {
       OrderService orderService;
       PaymentService paymentService;

       public StripeWebHookController(OrderService orderService,PaymentService paymentService){
           this.orderService=orderService;
           this.paymentService=paymentService;
       }
       @PostMapping("/")
       public void paymentLinkCreated(@RequestBody Event e) throws StripeException{
           System.out.println("Payment Link Created!");
           Event event = Event.retrieve(e.getId());
           System.out.println(event);
           long orderId=Long.parseLong(event.getRequest().getIdempotencyKey());
           System.out.println("Payment link generated for order ID: "+orderId);
       }

       @PostMapping("/paymentProcessed")
       public void paymentProcessed(@RequestBody Event e) throws StripeException, JsonProcessingException {
           Event event = Event.retrieve(e.getId());
           System.out.println(event);
           long orderId=Long.parseLong(event.getRequest().getIdempotencyKey());
           ObjectMapper objectMapper = new ObjectMapper();
           Charge charge=objectMapper.readValue(e.getObject(), Charge.class);
           if(e.getType().equals("charge.failed")){
              System.out.println("Payment Failed for orderId: "+ orderId);
              paymentService.updatePayment(orderId,charge.getId(),PaymentStatus.FAILED,PaymentMode.valueOf(charge.getPaymentMethod()));
              orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_FAILED);
           }
           else{
               System.out.println("Payment Success for orderId: "+ e.getRequest().getIdempotencyKey());
               paymentService.updatePayment(orderId,charge.getId(),PaymentStatus.CAPTURED,PaymentMode.valueOf(charge.getPaymentMethod()));
               orderService.updateOrderStatus(orderId,OrderStatus.PACKAGING);
           }
       }
}
