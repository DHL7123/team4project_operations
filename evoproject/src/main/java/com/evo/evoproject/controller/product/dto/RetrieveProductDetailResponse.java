package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductDetailResponse {
    private Product product;
}
