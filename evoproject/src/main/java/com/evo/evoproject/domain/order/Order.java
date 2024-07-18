package com.evo.evoproject.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Order {
    // Getters and Setters
    private Long order_no;
    private Long user_no;
    private int pro_no;
    private String order_name;
    private String order_address1;
    private String order_address2;
    private String order_phone;
    private String order_comment;
    private String pro_name;
    private String order_timestamp;
    private int pro_stock;
    private int order_payment;
    private int order_status;
    private int requestType;
    private int order_delivnum;
}
