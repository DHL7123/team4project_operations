package com.evo.evoproject.domain.product;

import lombok.Data;


@Data
public class Product {
    private int productNo;
    private String productName;
    private String productDes;
    private int imageId;
    private int categoryId;
    private String price;
    private int stock;
    private String date;
    private int viewCount;


}
