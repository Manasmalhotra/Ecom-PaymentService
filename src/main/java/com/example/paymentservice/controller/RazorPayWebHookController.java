package com.example.paymentservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/razorpayWebhook")
public class RazorPayWebHookController {
    @PostMapping("/")
    public void webhookRequest(@RequestBody String event){
        System.out.println("WebHook called "+event);
    }
}
