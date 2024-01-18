package com.example.paymentservice.models;

public enum PaymentMode {
    CARD("card"),
    NET_BANKING("netbanking"),
    WALLET("wallet"),
    EMI("emi"),
    UPI("upi");

    String value;

    PaymentMode(String value){
        this.value=value;
    }
}
