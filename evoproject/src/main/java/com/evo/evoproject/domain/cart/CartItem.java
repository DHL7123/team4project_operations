package com.evo.evoproject.domain.cart;

import com.evo.evoproject.domain.image.Image;
import lombok.Data;

@Data
public class CartItem {
    private int proNo;
    private String proName;
    private int proPrice;
    private int shipping;

    private Image mainImage;
}

