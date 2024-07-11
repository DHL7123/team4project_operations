package com.evo.evoproject.domain.cart;

import lombok.Data;

@Data
public class Product {
    private int proNo;
    private String proName;
    private int proPrice;
    private int shipping;
    private int soldout;
}


