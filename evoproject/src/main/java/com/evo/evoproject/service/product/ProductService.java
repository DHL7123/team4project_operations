package com.evo.evoproject.service.product;

import com.evo.evoproject.domain.product.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAllProducts(int page, int size);

    Product findProductByNo(int productNo);

    int countAllProducts(int pageSize);

    int countProductsByCategory (int categoryId, int pageSize);

    List<Product> findProductsByCategory(int categoryId, int page, int size);
}
