package com.evo.evoproject.controller.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.repository.product.ProductRepository;
import com.evo.evoproject.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class RestProductController {

    private final ProductService productService;

    @Autowired
    public RestProductController(ProductService productService) {
        this.productService = productService;
    }
    //모든 상품 조회
    @GetMapping
    public ResponseEntity<Map<String,Object>> etAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size){

        List<Product> products = productService.findAllProducts(page, size);


        Map<String,Object> response = new HashMap<>();
        response.put("products",products);
        response.put("currentPage",page);


        return ResponseEntity.ok(response);

    }






}
