package com.evo.evoproject.repository.product;

import com.evo.evoproject.domain.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {

    List<Product> selectAllProducts(int offset, int size);
    Product selectProductByNo(int productNo);
    int countAllProducts();

}
