package com.evo.evoproject.controller.product;


import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor

public class RestProductController {

//    private final ProductService productService;
//
//
//    //모든 상품 조회
//    @GetMapping("/all")
//    public ResponseEntity<RetrieveProductsResponse> getAllProducts(
//            @RequestParam(defaultValue = "pro_date_desc") String sort,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "16") int size) {
//
//        RetrieveProductsResponse response = productService.getAllProducts(sort, page, size);
//        return ResponseEntity.ok(response);
//    }






}
