package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductsResponse {
    private List<Product> products;
    private String sort;
    private int currentPage;
    private int totalPages;

    public static RetrieveProductsResponse successfulResponse(List<Product> products, String sort, int currentPage, int totalPages) {
        return new RetrieveProductsResponse(products, sort, currentPage, totalPages);
    }
    public static RetrieveProductsResponse serviceException(List<Product> products, String sort, int currentPage, int totalPages) {
        return new RetrieveProductsResponse(products, sort, currentPage, totalPages);
    }
}