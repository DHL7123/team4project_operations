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
    private int orderStatus;    // 주문상태 (0: 결제 대기, 1: 배송준비중, 2: 배송중, 3: 결제취소/환불, 4: 배송완료)
    private String orderDelivnum;
    private int requestType;    //결제 취소/환불 요청 타입 (0: 아무 요청 없음, 1: 결제 취소 요청, 2: 환불 요청, 3: 결제 취소 요청 승인, 4: 환불 요청 승인 5: 환불 요청 거절)

}
