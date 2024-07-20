package com.evo.evoproject.domain.order;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class UserOrder {
    private int orderNo;
    private int userNo;
    private int proNo;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private int orderPhone;
    private String orderComment;
    private Timestamp orderTimestamp;
    private int orderPayment;
    private int orderStatus;    // 주문상태 (0: 결제 대기, 1: 배송준비중, 2: 배송중, 3: 결제취소/환불, 4: 배송완료)
    private String orderDelivnum;
    private int requestType;    //
    private List<Orderitem> items;
}