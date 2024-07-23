package com.evo.evoproject.mapper.product;


import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {
    List<Product> findAllProductsUser(@Param("sort")String sort, @Param("offset")int offset, @Param("size")int size);
    List<Product> findAllProductsAdmin(@Param("sort")String sort, @Param("offset")int offset, @Param("size")int size, @Param("soldout") Integer soldout);
    Product findProductByNo(@Param("productNo") int productNo);
    int countProducts();
    int countProductsByCategory(int categoryId);
    List<Product> findProductsByCategory(@Param("sort")String sort, @Param("categoryId") int categoryId, @Param("offset") int offset, @Param("size") int size);
    void incrementProductViewCount(@Param("productNo")int productNo, @Param("count")int count);
    List<Product> findTopProductsByCategory(@Param("categoryId") int categoryId, @Param("productNo") int productNo);
    List<Product> findProductByName(@Param("productName") String input, @Param("sort")String sort, @Param("offset")int offset, @Param("size")int size);
    int countByProductsName(String productName);
    void addProduct(Product product);
    void updateProductMainImage(Map<String, Object> params);
    void saveProductImageMapping(@Param("productNo") int productNo, @Param("imageId") int imageId);
    void deleteProductImages(int productNo);
    void deleteProduct(int productNo);
    void updateProduct(Product product);
    int countProductsAdmin(Integer soldout);
    void deleteProductImageMapping( @Param("imageId") int imageId);


}