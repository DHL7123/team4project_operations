package com.evo.evoproject.service.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // 모든 상품 조회
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts(int page, int size) {
//        List<Product> response = new ArrayList<>();
        int offset = (page - 1) * size; //출력할 상품번호 계산
        return productRepository.findAllProducts(offset, size);
    }

    // 상품 페이지수 조회
    @Override
    @Transactional(readOnly = true)
    public int countAllProducts(int pageSize) {
        int totalCount = productRepository.countAllProducts();
        return (totalCount + pageSize - 1) / pageSize; //페이지수 계산
    }


    @Override
    @Transactional(readOnly = true)
    public int countProductsByCategory(int categoryId, int pageSize) {
        int totalCount = productRepository.countProductsByCategory(categoryId);
        return (totalCount + pageSize - 1) / pageSize;
    }


    // 상품 번호로 특정 상품 조회
    @Override
    @Transactional
    public Product findProductByNo(int productNo) {
        increaseViewCount(productNo);
        return productRepository.findProductByNo(productNo);
    }
    // 카테고리별 상품 조회
    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByCategory(int categoryId, int page, int size) {
        int offset = (page - 1) * size; //출력할 상품번호 계산
        return productRepository.findProductsByCategory(categoryId, offset, size);
    }


    private void increaseViewCount(int productNo) {
        productRepository.incrementProductViewCount(productNo);
    }
}


