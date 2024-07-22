package com.evo.evoproject.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCallbackRequest {
    private String transactionId;
    private int amount;
    private String orderId;
    private String orderComment;
    private String orderAddress1;
    private String orderAddress2;


    // Getters and Setters
}