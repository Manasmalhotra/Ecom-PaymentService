package com.example.paymentservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    @Id
    String id;
    int amount;
    String currency;
    PaymentMode paymentMode;
    PaymentStatus paymentStatus;
}
