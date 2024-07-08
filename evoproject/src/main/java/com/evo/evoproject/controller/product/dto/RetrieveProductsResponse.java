package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductsResponse {
    private List<Product> products;
    private int currentPage;
    private int totalPages;
    private String result;
    private String message;
}