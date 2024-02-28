package com.example.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiateRequestDTO {
     long orderId;
     long amount;
     long userId;
}
