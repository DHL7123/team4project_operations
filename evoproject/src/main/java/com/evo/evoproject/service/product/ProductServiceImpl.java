package com.evo.evoproject.service.product;

import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // @Autowired 의존성 주입을 통해 초기화
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // 모든 상품 조회
    @Override
    public List<Product> getAllProducts(int page, int size) {
        int offset = (page - 1) * size; //출력할 상품번호 계산
        return productRepository.selectAllProducts(offset, size);
    }
    //
    @Override
    public int getTotalPages(int pageSize) {
        int totalCount = productRepository.countAllProducts();
        return (totalCount + pageSize - 1) / pageSize; //페이지수 계산
    }
    // 상품 번호로 특정 상품 조회
    @Override
    public Product getProductByNo(int productNo) {
        return productRepository.selectProductByNo(productNo);
    }



}
