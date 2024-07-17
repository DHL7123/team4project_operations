package com.evo.evoproject.service.product;

import com.evo.evoproject.controller.product.dto.AdminRetrieveProductResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.product.RetrieveProduct;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface ProductService {
    RetrieveProductsResponse getProductsUser(String sort, int page, int size);
    AdminRetrieveProductResponse getProductsAdmin(String sort, int page, int size, Integer soldout);
    RetrieveProductDetailResponse getProductByNo(int productNo);
    RetrieveProductsResponse getProductsByCategory(String sort, int categoryId, int page, int size);
    RetrieveProductsResponse getTopProductsByCategory(int categoryId, int productNo);
    RetrieveProductsResponse searchProductByName(String input, String sort, int page, int size);
    void deleteProductWithImages(Long id);
    List<String> getProductImageUrls(Long id);
    void saveProductWithImages(RetrieveProduct product, List<MultipartFile> images);
    void updateProductWithImages(RetrieveProduct product, List<MultipartFile> newImages, List<Integer> imagesToDelete);
}

