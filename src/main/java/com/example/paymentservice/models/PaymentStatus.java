package com.example.paymentservice.models;

public enum PaymentStatus {
    CREATED("created"),
    AUTHORIZED("authorized"),
    CAPTURED("captured"),
    REFUNDED("refunded"),
    FAILED("failed");
    String value;
    PaymentStatus(String value){
        this.value=value;
    }
}
