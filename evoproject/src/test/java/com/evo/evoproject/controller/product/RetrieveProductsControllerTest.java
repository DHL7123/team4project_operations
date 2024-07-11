package com.evo.evoproject.controller.product;

import com.evo.evoproject.controller.product.RetrieveProductsController;
import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetrieveProductsControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private RetrieveProductsController retrieveProductsController;

    @Mock
    private Model model;

    private Product product;
    private RetrieveProductDetailResponse response;
    private RetrieveProductsResponse relatedProductsResponse;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductNo(20);
        product.setProductName("멋진 의자");
        product.setProductDes("멋들어진 의자");
        product.setCategoryId(1);
        product.setPrice("1000000");
        product.setStock(1);
        product.setDate(LocalDateTime.now());
        product.setViewCount(10);
        product.setShipping(50000);
        product.setSoldout(false);

        Image image1 = new Image(1, "https://example.com/image01.jpg", "상품");
        Image image2 = new Image(2, "https://example.com/image02.jpg", "상품");

        response = new RetrieveProductDetailResponse();
        response.setProduct(product);
        response.setImages(List.of(image1, image2));
        response.setRelatedProducts(List.of());

        relatedProductsResponse = new RetrieveProductsResponse();
        relatedProductsResponse.setProducts(List.of(product));

        when(productService.getProductByNo(20)).thenReturn(response);
        when(productService.getTopProductsByCategory(1, 20)).thenReturn(relatedProductsResponse);
    }

    @Test
    void testGetProductByNo() {
        String viewName = retrieveProductsController.getProductByNo(20, model);
        assertEquals("productDetail", viewName);
        verify(model).addAttribute("productDetailResponse", response);
        verify(model).addAttribute("relatedProducts", relatedProductsResponse.getProducts());
    }
}
