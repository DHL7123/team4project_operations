package com.evo.evoproject.controller.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private int userNo;
    private int proNo; // 필요 시 추가
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private String orderPhone;
    private String orderComment;
    private int orderPayment;
    private List<RetrieveOrderItemRequest> items;
    private int orderStatus;
    private String orderDelivnum;
    private int requestType;
    private int quantity;
}
