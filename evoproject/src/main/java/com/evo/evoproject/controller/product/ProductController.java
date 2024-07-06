package com.evo.evoproject.controller.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    //의존성 주입
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //모든 상품 조회
    @GetMapping

      public String getAllProducts(
              @RequestParam(defaultValue = "1") int page,
              @RequestParam(defaultValue = "16") int size,
              Model model) {
        log.info("getAllProducts. page {}, size {}", page, size);
        List<Product> products = productService.getAllProducts(page, size);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        return "productList";
    }
    //상품번호로 특정 상품 조회
    @GetMapping("/{no}")
      public String getProductByNo (@PathVariable int no, Model model){
        Product product = productService.getProductByNo(no);
       if(product != null) {
           model.addAttribute("product", product);
           return "productDetail";
       }else{
           return "error";
       }

    }
}
