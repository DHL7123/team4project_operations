package com.evo.evoproject.controller.product;

import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class RetrieveProductsController {

    private final ProductService productService;


    /**
     * 모든 제품 목록을 가져오는 메서드
     *
     * @param sort  상품 정렬 (기본값: 등록일)
     * @param page  페이지 번호 (기본값: 1)
     * @param size  페이지당 항목 수 (기본값: 16)
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 제품 목록 뷰 이름
     */
    @GetMapping
    public String getProducts(
            @RequestParam(defaultValue = "pro_date_desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            Model model) {
        log.info("모든 제품 목록 요청 - 정렬기준: {}, 페이지: {}, 사이즈: {}", sort, page, size);
        try {
            RetrieveProductsResponse response = productService.getProductsUser(sort, page, size);
            model.addAttribute("productsResponse", response);
            model.addAttribute("products", response.getProducts());
            return "product/list";
        } catch (Exception e) {
            log.error("제품 목록을 가져오는 중 오류 발생", e);
            model.addAttribute("error", "제품 목록을 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    /**
     * 카테고리별 제품 목록을 가져오는 메서드
     *
     * @param sort       상품 정렬 (기본값: 등록일)
     * @param page       페이지 번호 (기본값: 1)
     * @param size       페이지당 항목 수 (기본값: 16)
     * @param categoryId 카테고리 ID
     * @param model      뷰에 데이터를 전달하기 위한 모델 객체
     * @return 카테고리별 제품 목록 뷰 이름
     */
    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory(
            @RequestParam(defaultValue = "pro_date_desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @PathVariable int categoryId,
            Model model) {
        log.info("카테고리별 제품 목록 요청 - 정렬기준: {} 카테고리 ID: {}, 페이지: {}, 사이즈: {}", sort, categoryId, page, size);
        try {
            RetrieveProductsResponse response = productService.getProductsByCategory(sort, categoryId, page, size);
            model.addAttribute("productsResponse", response);
            model.addAttribute("products", response.getProducts());
            model.addAttribute("categoryId", categoryId);
            return "product/list";
        } catch (Exception e) {
            log.error("카테고리별 제품 목록을 가져오는 중 오류 발생", e);
            model.addAttribute("error", "카테고리별 제품 목록을 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    /**
     * 제품 번호로 특정 제품의 상세 정보를 가져오는 메서드
     *
     * @param productNo 제품 번호
     * @return 제품 상세 정보 뷰 이름
     */
    @GetMapping("/detail/{productNo}")
    public String getProductByNo(@PathVariable int productNo, Model model) {
        log.info("제품 상세 정보 요청 - 제품 번호: {}", productNo);
        try {
            RetrieveProductDetailResponse response = productService.getProductByNo(productNo);
            log.info("제품 상세 정보 응답: {}", response); // 제품 상세 정보 로그 출력

            int categoryId = response.getProduct().getCategoryId();
            RetrieveProductsResponse relatedProductsResponse = productService.getTopProductsByCategory(categoryId, productNo);

            model.addAttribute("productDetailResponse", response);
            model.addAttribute("relatedProducts", relatedProductsResponse.getProducts());
            return "product/detail";
        } catch (Exception e) {
            log.error("제품 상세 정보를 가져오는 중 오류 발생", e);
            model.addAttribute("error", "제품 상세 정보를 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }
    /**
     * 검색값에 맞는 제품을 가져오는 메서드
     * @param sort         상품 정렬 (기본값: 등록일)
     * @param page         페이지 번호 (기본값: 1)
     * @param size         페이지당 항목 수 (기본값: 16)
     * @param input  입력값
     * @param model       뷰에 데이터를 전달하기 위한 모델 객체
     * @return 검색값에 맞는 제품 목록 뷰 이름
     */
    @GetMapping("/search/{input}")
    public String getProductByName(
            @PathVariable String input,
            @RequestParam(defaultValue = "pro_date_desc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            Model model) {
        log.info("검색값에 맞는 제품 목록 요청 - 정렬기준: {} 상품이름: {}, 페이지: {}, 사이즈: {}", input, sort, page, size);
        try {
            RetrieveProductsResponse response = productService.searchProductByName(input, sort, page, size);
            model.addAttribute("productsResponse", response);
            model.addAttribute("products", response.getProducts());
            model.addAttribute("productName", input);
            return "product/list";
        } catch (Exception e) {
            log.error("검색값에 맞는 제품 목록을 가져오는 중 오류 발생", e);
            model.addAttribute("error", "검색값에 맞는 제품 목록을 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

}

