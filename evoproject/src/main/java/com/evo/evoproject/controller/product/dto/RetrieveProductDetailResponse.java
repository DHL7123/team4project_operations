package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductDetailResponse {
    private Product product;
    private List<Image> images;
    private List<Product> relatedProducts;
}
