package com.evo.evoproject.repository.product;

import com.evo.evoproject.domain.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {

    List<Product> findAllProducts(int offset, int size);
    Product findProductByNo(int productNo);
    int countAllProducts();
    List<Product> findProductsByCategory(int categoryId, int offset, int size);
    void incrementProductViewCount(int productNo);
    int countProductsByCategory(int categoryId);
}
