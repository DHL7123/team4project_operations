package com.evo.evoproject.service.product;


import com.evo.evoproject.Mapper.product.ImageMapper;
import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.Mapper.product.ProductMapper;
import com.evo.evoproject.domain.product.RetrieveProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final Map<Integer, AtomicInteger> viewCountMap = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getAllProducts(String sort, int page, int size) {
        log.info("모든 제품 목록을 가져오는 서비스 - 정렬기준: {}, 페이지: {}, 사이즈: {}", sort, page, size);
        try {
            int offset = (page - 1) * size;
            List<RetrieveProduct> products = productMapper.findAllProducts(sort, offset, size);
            int totalProducts = productMapper.countAllProducts();
            int totalPages = (totalProducts + size - 1) / size;
            return new RetrieveProductsResponse(products, sort, page, totalPages);
        } catch (Exception e) {
            log.error("모든 제품 목록을 가져오는 중 오류 발생", e);
            throw e;
        }
    }


    @Transactional
    @Override
    public RetrieveProductDetailResponse getProductByNo(int productNo) {
        log.info("특정 제품 상세 정보를 가져오는 서비스 - 제품 번호: {}", productNo);
        try {
            RetrieveProduct product = productMapper.findProductByNo(productNo);
            if (product == null) {
                log.error("제품 번호 {}에 해당하는 제품을 찾을 수 없습니다.", productNo);
                throw new NullPointerException("Product with productNo " + productNo + " not found");
            }
            increaseViewCount(productNo);

            log.info("이미지 쿼리 실행 전 - 제품 번호: {}", productNo);
            List<Image> images = imageMapper.findImagesByProductNo(productNo);
            log.info("이미지 데이터: {}", images);


            product.setImages(images);
            List<RetrieveProduct> relatedProducts = productMapper.findTopProductsByCategory(product.getCategoryId(), productNo);

            return new RetrieveProductDetailResponse(product, images, relatedProducts);
        } catch (Exception e) {
            log.error("특정 제품 상세 정보를 가져오는 중 오류 발생", e);
            throw e;
        }
    }




    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getProductsByCategory(String sort, int categoryId, int page, int size) {
        log.info("카테고리별 제품 목록을 가져오는 서비스 - 정렬 기준: {}, 카테고리 ID: {}, 페이지: {}, 사이즈: {}", sort, categoryId, page, size);
        try {
            int offset = (page - 1) * size;
            List<RetrieveProduct> products = productMapper.findProductsByCategory(sort, categoryId, offset, size);
            int totalProducts = productMapper.countProductsByCategory(categoryId);
            int totalPages = (totalProducts + size - 1) / size;
            return new RetrieveProductsResponse(products, sort, page, totalPages);
        } catch (Exception e) {
            log.error("카테고리별 제품 목록을 가져오는 중 오류 발생", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getTopProductsByCategory(int categoryId, int productNo) {
        log.info("카테고리별 상위 제품을 조회하는 서비스 - 카테고리 ID: {}", categoryId);
        try {
            List<RetrieveProduct> products = productMapper.findTopProductsByCategory(categoryId, productNo);
            return new RetrieveProductsResponse(products, "viewCount_desc", 1, 1);
        } catch (Exception e) {
            log.error("카테고리별 상위 제품을 조회하는 중 오류 발생", e);
            throw e;
        }
    }

    private void increaseViewCount(int productNo) {
        log.info("제품 번호 {}의 조회수를 증가시킵니다.", productNo);
        viewCountMap.computeIfAbsent(productNo, k -> new AtomicInteger(0)).incrementAndGet();
    }

    @Scheduled(fixedRate = 60000)
    public void updateViewCounts() {
        log.info("조회수 업데이트 작업을 시작합니다.");
        viewCountMap.forEach((productNo, count) -> {
            int currentCount = count.getAndSet(0);
            if (currentCount > 0) {
                log.info("제품 번호 {}의 조회수를 {}만큼 업데이트합니다.", productNo, currentCount);
                productMapper.incrementProductViewCount(productNo, currentCount);
            }
        });
        log.info("조회수 업데이트 작업을 완료했습니다.");
    }
}
