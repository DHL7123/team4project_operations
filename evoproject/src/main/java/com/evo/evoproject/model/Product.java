package com.evo.evoproject.model;

import lombok.Data;
import java.util.Date;

@Data
public class Product {
    private int proNo;
    private String proName;
    private int proPrice;
    private int shipping;
    private int soldout;
}


