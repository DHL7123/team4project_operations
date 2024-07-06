package com.evo.evoproject.service.product;

import com.evo.evoproject.domain.product.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductByNo(int productNo);

}
