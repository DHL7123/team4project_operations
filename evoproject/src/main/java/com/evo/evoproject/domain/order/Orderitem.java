package com.evo.evoproject.domain.order;

import lombok.Data;

@Data
public class Orderitem {
    private int orderNo;
    private int productNo;
    private String productName;
    private int quantity;
    private int price;
    private int shippingCost;
    private String image;
}


