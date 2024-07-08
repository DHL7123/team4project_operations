package com.evo.evoproject.controller.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //모든 상품 조회
    @GetMapping
      public String getAllProducts(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "16") int size,
                                   Model model) {
        List<Product> products = productService.findAllProducts(page, size);
        int totalPages = productService.countAllProducts(size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        log.info("getAllProducts. page = {}, size = {}", page, size);
        return "productList";
        // return productService.listProducts_v2(page, size);
    }
    // 상품번호로 특정 상품 조회
    @GetMapping("/detail/{no}")
    public String getProductByNo(@PathVariable int no, Model model) {
        Product product = productService.findProductByNo(no);
        if (product != null) {
            model.addAttribute("product", product);
            return "productDetail";
        } else {
            return "error";
        }
    }
    // 카테고리별 상품 조회
    @GetMapping("/category/{id}")
    public String getProductsByCategoryId(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            Model model) {
        try {
            List<Product> products = productService.findProductsByCategory(id, page, size);
            int totalPages = productService.countAllProducts(size);
            model.addAttribute("products", products);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            return "productList";
        } catch (Exception e) {
            log.error("Failed to fetch products for category: {} with error: {}", id, e.getMessage());
            return "error";
        }
    }


    }

// Client -> OrderRequestDTO
// Controller -> . OrderResponseDto
// Domain -> DTO
// Infra