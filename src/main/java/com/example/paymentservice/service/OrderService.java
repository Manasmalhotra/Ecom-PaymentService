package com.example.paymentservice.service;


import com.example.paymentservice.models.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@Service
@FeignClient(name="ORDER-SERVICE")
public interface OrderService {
    @PutMapping("/updateOrder/{orderId}")
    void updateOrderStatus(@PathVariable long orderId, OrderStatus orderStatus);
}
