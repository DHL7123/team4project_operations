package com.evo.evoproject.controller.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int orderNo;
    private int userNo;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private int orderPhone;
    private String orderComment;
    private Date orderTimestamp;
    private int orderPayment;
    private int orderStatus;
    private String orderDelivnum;
    private int requestType;
    private List<RetrieveOrderItemRequest> items;
}