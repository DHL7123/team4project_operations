package com.evo.evoproject.Mapper.product;


import com.evo.evoproject.domain.product.RetrieveProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<RetrieveProduct> findAllProducts(@Param("sort")String sort, @Param("offset")int offset, @Param("size")int size);
    RetrieveProduct findProductByNo(@Param("productNo") int productNo);
    int countAllProducts();
    int countProductsByCategory(int categoryId);
    List<RetrieveProduct> findProductsByCategory(@Param("sort")String sort, @Param("categoryId") int categoryId, @Param("offset") int offset, @Param("size") int size);
    void incrementProductViewCount(@Param("productNo")int productNo, @Param("count")int count);
    List<RetrieveProduct> findTopProductsByCategory(@Param("categoryId") int categoryId, @Param("productNo") int productNo);
    List<RetrieveProduct> findProductByName(@Param("productName") String input,@Param("sort")String sort, @Param("offset")int offset, @Param("size")int size);
    int countByProductsName(String productName);
}
