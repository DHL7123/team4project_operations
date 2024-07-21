package com.evo.evoproject.controller.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
        private int orderNo;
        private int userNo;
        private int proNo;
        private String orderName;
        private String orderAddress1;
        private String orderAddress2;
        private String orderPhone;
        private String orderComment;
        private Timestamp orderTimestamp;
        private String orderPayment;
        private String orderStatus;
        private String orderDelivnum;
        private String requestType;
        private int quantity;

}