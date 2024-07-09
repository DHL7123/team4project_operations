package com.evo.evoproject.domain.product;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Product {
    private int productNo;
    private String productName;
    private String productDes;
    private int categoryId;
    private String price;
    private int stock;
    private LocalDateTime date;
    private int viewCount;
    private int shipping;
    private boolean soldout;
    private int imageId;
    private String imageUrl;


}
