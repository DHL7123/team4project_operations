package com.evo.evoproject.controller.product;

import com.evo.evoproject.controller.product.dto.RegisterProductRequest;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.product.RetrieveProduct;
import com.evo.evoproject.service.image.ImageService;
import com.evo.evoproject.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final ProductService productService;
    private final ImageService imageService;

    @GetMapping
    public String getAllProducts(@RequestParam(defaultValue = "id") String sort,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        RetrieveProductsResponse response = productService.getAllProducts(sort, page, size);
        model.addAttribute("products", response.getProducts());
        model.addAttribute("currentPage", response.getCurrentPage());
        model.addAttribute("totalPages", response.getTotalPages());
        return "product/adminProductList";
    }

    @GetMapping("/form")
    public String showProductForm(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("product", new RetrieveProduct());
            model.addAttribute("isEdit", false);
        } else {
            RetrieveProduct product = productService.getProductByNo(id.intValue()).getProduct();
            List<String> imageUrls = productService.getProductImageUrls(id);
            model.addAttribute("product", product);
            model.addAttribute("isEdit", true);
            model.addAttribute("imageUrls", imageUrls);
        }
        return "product/productForm";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute RetrieveProduct product,
                              @RequestParam("images") List<MultipartFile> images) {
        productService.saveProductWithImages(product, images);
        return "redirect:/admin/product";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute RetrieveProduct product,
                                @RequestParam("images") List<MultipartFile> images) {
        productService.updateProductWithImages(product, images);
        return "redirect:/admin/product";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductWithImages(id);
        } catch (Exception e) {
            log.error("Error deleting product with id: " + id, e);
            return "redirect:/admin/product?error=true";
        }
        return "redirect:/admin/product";
    }
}
