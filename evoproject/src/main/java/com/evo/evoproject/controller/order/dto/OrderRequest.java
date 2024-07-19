package com.evo.evoproject.controller.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int userNo;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private int orderPhone;
    private String orderComment;
    private int orderPayment;
    private List<RetrieveOrderItemRequest> items;
}