package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.product.RetrieveProduct;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterProductRequest {
    private List<RetrieveProduct> products = new ArrayList<>(); // 기본값으로 초기화
    private String imageUrl;
}