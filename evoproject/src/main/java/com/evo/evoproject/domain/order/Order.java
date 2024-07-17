package com.evo.evoproject.domain.order;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private int orderNo;
    private int userNo;
    private int proNo;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private int orderPhone;
    private String orderComment;
    private Date orderTimestamp;
    private int orderPayment;
    private int orderStatus;
    private String orderDelivnum;

}
