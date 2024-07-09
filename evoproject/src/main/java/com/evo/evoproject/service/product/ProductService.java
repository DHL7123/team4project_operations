package com.evo.evoproject.service.product;

import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.product.Product;
import java.util.List;

public interface ProductService {
    RetrieveProductsResponse getAllProducts(String sort,int page,int size);
    RetrieveProductDetailResponse getProductByNo(int productNo);
    RetrieveProductsResponse getProductsByCategory(String sort,int categoryId,int page, int size);
    RetrieveProductsResponse getTopProductsByCategory(int categoryId, int productNo);

}
