package com.evo.evoproject.service.product;

import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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

    private final ProductRepository productRepository;
    private final Map<Integer, AtomicInteger> viewCountMap = new ConcurrentHashMap<>();

    /**
     * 모든 제품을 가져오는 서비스 메서드
     * @param sort  정렬 기준
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 제품 목록 응답 객체
     */
    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getAllProducts(String sort,int page, int size) {
        log.info("모든 제품 목록을 가져오는 서비스 - 정렬기준: {}, 페이지: {}, 사이즈: {}",sort, page, size);
        try {
            int offset = (page - 1) * size; // offset 계산
            List<Product> products = productRepository.findAllProducts(sort, offset, size);
            int totalProducts = productRepository.countAllProducts();

            RetrieveProductsResponse response = new RetrieveProductsResponse();
            response.setProducts(products);
            response.setSort(sort);
            response.setCurrentPage(page);
            response.setTotalPages((totalProducts + size - 1) / size);
            return response;
        } catch (Exception e) {
            log.error("모든 제품 목록을 가져오는 중 오류 발생", e);
            throw e;  // 예외를 다시 던져 호출한 곳에서 처리할 수 있게 합니다.
        }
    }
    /**
     * 제품 번호로 특정 제품을 가져오는 서비스 메서드
     *
     * @param productNo 제품 번호
     * @return 제품 상세 정보 응답 객체
     */
    @Transactional
    @Override
    public RetrieveProductDetailResponse getProductByNo(int productNo) {
        log.info("특정 제품 상세 정보를 가져오는 서비스 - 제품 번호: {}", productNo);
        try {
            Product product = productRepository.findProductByNo(productNo);
            increaseViewCount(productNo);
            RetrieveProductDetailResponse response = new RetrieveProductDetailResponse();
            response.setProduct(product);
            return response;
        } catch (Exception e) {
            log.error("특정 제품 상세 정보를 가져오는 중 오류 발생", e);
            throw e;  // 예외를 다시 던져 호출한 곳에서 처리할 수 있게 합니다.
        }
    }
    /**
     * 카테고리별 제품을 가져오는 서비스 메서드
     * @param sort 카테고리 ID
     * @param categoryId 카테고리 ID
     * @param page 페이지 번호
     * @param size 페이지당 항목 수
     * @return 카테고리별 제품 목록 응답 객체
     */
    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getProductsByCategory(String sort,int categoryId, int page, int size) {
        log.info("카테고리별 제품 목록을 가져오는 서비스 - 정렬 기준: {}, 카테고리 ID: {}, 페이지: {}, 사이즈: {}",sort, categoryId, page, size);
        try {
            int offset = (page - 1) * size; // offset 계산
            List<Product> products = productRepository.findProductsByCategory(sort, categoryId, offset, size);
            int totalProducts = productRepository.countProductsByCategory(categoryId);

            RetrieveProductsResponse response = new RetrieveProductsResponse();
            response.setSort(sort);
            response.setProducts(products);
            response.setCurrentPage(page);
            response.setTotalPages((totalProducts + size - 1) / size);
            return response;
        } catch (Exception e) {
            log.error("카테고리별 제품 목록을 가져오는 중 오류 발생", e);
            throw e;  // 예외를 다시 던져 호출한 곳에서 처리할 수 있게 합니다.
        }
    }


    /**
     * 특정 제품의 조회수를 증가시키는 메서드
     *
     * @param productNo 제품 번호
     */
    private void increaseViewCount(int productNo) {
        log.info("제품 번호 {}의 조회수를 증가시킵니다.", productNo);
        viewCountMap.computeIfAbsent(productNo, k -> new AtomicInteger(0)).incrementAndGet();
    }


    /**
     * 조회수를 데이터베이스에 업데이트하는 스케줄러 메서드
     * 1분마다 실행되도록 설정됨
     */
    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void updateViewCounts() {
        log.info("조회수 업데이트 작업을 시작합니다.");
        viewCountMap.forEach((productNo, count) -> {
            int currentCount = count.getAndSet(0);
            if (currentCount > 0) {
                log.info("제품 번호 {}의 조회수를 {}만큼 업데이트합니다.", productNo, currentCount);
                productRepository.incrementProductViewCount(productNo, currentCount);
            }
        });
        log.info("조회수 업데이트 작업을 완료했습니다.");
    }
}

//void productsSortedByPrice(@Param("sort")String sort);
//void productsSortedByDate();
//void productsSortedByViewCount();