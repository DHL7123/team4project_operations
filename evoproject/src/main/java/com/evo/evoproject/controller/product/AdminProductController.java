package com.evo.evoproject.controller.product;

import com.evo.evoproject.controller.product.dto.AdminRetrieveProductResponse;
import com.evo.evoproject.domain.product.Product;
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

    @GetMapping
    public String getProducts(@RequestParam(defaultValue = "pro_date_desc") String sort,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Integer soldout,
                              Model model) {
        AdminRetrieveProductResponse response = productService.getProductsAdmin(sort, page, size,soldout);
        model.addAttribute("productsResponse", response);
        model.addAttribute("products", response.getProducts());
        return "product/adminProductList";
    }

    @GetMapping("/form")
    public String showProductForm(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("product", new Product());
            model.addAttribute("isEdit", false);
        } else {
            Product product = productService.getProductByNo(id.intValue()).getProduct();
            List<String> imageUrls = productService.getProductImageUrls(id);
            model.addAttribute("product", product);
            model.addAttribute("isEdit", true);
            model.addAttribute("imageUrls", imageUrls);
        }
        return "product/productForm";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
                              @RequestParam("images") List<MultipartFile> images) {
        productService.saveProductWithImages(product, images);
        return "redirect:/admin/product";
    }

    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute Product product,
                                @RequestParam("images") List<MultipartFile> images,
                                @RequestParam(value = "imagesToDelete", required = false) List<Integer> imagesToDelete) {
        productService.updateProductWithImages(product, images, imagesToDelete);
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
