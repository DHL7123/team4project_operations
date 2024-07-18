package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.product.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterProductRequest {
    private List<Product> products = new ArrayList<>(); // 기본값으로 초기화
    private String imageUrl;
}