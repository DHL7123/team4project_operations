package com.evo.evoproject.service.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // @Autowired 의존성 주입을 통해 초기화
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // 모든 상품 조회 메서드
    @Override
    public List<Product> getAllProducts(int page, int size) {
        int offset = (page - 1) * size;
        return productRepository.selectAllProducts(offset, size);
    }
    // 상품 번호로 특정 상품 조회
    @Override
    public Product getProductByNo(int productNo) {
        return productRepository.selectProductByNo(productNo);
    }
}
