package com.evo.evoproject.controller.product.dto;

import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.domain.product.RetrieveProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductDetailResponse {
    private RetrieveProduct product;
    private List<Image> images;
    private List<RetrieveProduct> relatedProducts;
}
