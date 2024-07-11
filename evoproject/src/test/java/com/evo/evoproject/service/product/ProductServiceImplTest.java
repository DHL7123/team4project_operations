package com.evo.evoproject.service.product;

import com.evo.evoproject.Mapper.image.ImageMapper;
import com.evo.evoproject.Mapper.product.ProductMapper;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductNo(20);
        testProduct.setProductName("Test Product");

        // 필요한 스터빙 설정
        when(productMapper.findProductByNo(20)).thenReturn(testProduct);
        when(imageMapper.findImagesByProductNo(20)).thenReturn(Collections.emptyList());
        when(productMapper.findTopProductsByCategory(anyInt(), anyInt())).thenReturn(Collections.emptyList());
    }

    @Test
    void testGetProductByNo_ProductFound() {
        RetrieveProductDetailResponse response = productService.getProductByNo(20);
        assertNotNull(response);
        assertNotNull(response.getProduct());
        assertNotNull(response.getImages());
        assertNotNull(response.getRelatedProducts());
    }
}