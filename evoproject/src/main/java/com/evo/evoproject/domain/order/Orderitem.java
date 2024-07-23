package com.evo.evoproject.domain.order;

import lombok.Data;

import java.util.Date;

@Data
public class Orderitem {
    private int orderNo;
    private int productNo;
    private int quantity;
    private int price;
    private int shipping;
    private String productName;
    private String mainImage;
    private int orderPayment;
    private int orderStatus;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private String orderPhone;
    private String orderComment;
    private Date orderTimestamp;
    private String orderDelivnum;
    private int requestType;
}

