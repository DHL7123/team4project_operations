package com.evo.evoproject.domain.order;

import lombok.Data;

@Data
public class Orderitem {
    private int orderNo;
    private int productNo;
    private int quantity;
    private int price;
    private int shipping;
    private String productName;
    private String mainImage;
}


