package com.evo.evoproject.controller.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
//@RestController
@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
      public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
//      return ResponseEntity.ok(products);
        return "productList";
    }

    @GetMapping("/{no}")
//    public ResponseEntity<Product> getProductByNo(@PathVariable int no) {
      public String getProductByNo (@PathVariable int no, Model model){
        Product product = productService.getProductByNo(no);
        model.addAttribute("product",product);
        return "productDetail";
//        if (product != null) {
//            return ResponseEntity.ok(product);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}
