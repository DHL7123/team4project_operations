package com.evo.evoproject.domain.order;

import lombok.Data;

import java.util.List;

@Data
public class UserOrder {
    private List<Orderitem> items;
    private String orderName;
    private String orderAddress1;
    private String orderAddress2;
    private String orderRecipient;
    private int orderPhone;
    private String orderComment;
}