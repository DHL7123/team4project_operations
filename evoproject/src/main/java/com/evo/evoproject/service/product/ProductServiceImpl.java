package com.evo.evoproject.service.product;

import com.evo.evoproject.controller.product.dto.AdminRetrieveProductResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductDetailResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.domain.product.RetrieveProduct;
import com.evo.evoproject.mapper.product.ImageMapper;
import com.evo.evoproject.mapper.product.ProductMapper;
import com.evo.evoproject.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j

public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final ImageService imageService;
    private final Map<Integer, AtomicInteger> viewCountMap = new ConcurrentHashMap<>();

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getProductsUser(String sort, int page, int size) {
        log.info("모든 제품 목록을 가져오는 서비스 - 정렬기준: {}, 페이지: {}, 사이즈: {}", sort, page, size);

        int offset = (page - 1) * size;
        int totalProducts = productMapper.countProducts();
        int totalPages = (totalProducts + size - 1) / size;

        List<RetrieveProduct> products = productMapper.findAllProductsUser(sort, offset, size);

        return new RetrieveProductsResponse(products, sort, page, totalPages);
    }

    @Override
    public AdminRetrieveProductResponse getProductsAdmin(String sort, int page, int size, Integer soldout) {
        log.info("모든 제품 목록을 가져오는 서비스 - 정렬기준: {}, 페이지: {}, 사이즈: {}", sort, page, size);
        int offset = (page - 1) * size;
        List<RetrieveProduct> products = productMapper.findAllProductsAdmin(sort, offset, size,soldout);
        int totalProducts = productMapper.countProductsAdmin(soldout);
        int totalPages = (totalProducts + size - 1) / size;
        return new AdminRetrieveProductResponse(products, sort,soldout, page, totalPages);
    }

    @Transactional
    @Override
    public RetrieveProductDetailResponse getProductByNo(int productNo) {
        log.info("특정 제품 상세 정보를 가져오는 서비스 - 제품 번호: {}", productNo);
        RetrieveProduct product = productMapper.findProductByNo(productNo);
        if (product == null) {
            log.error("제품 번호 {}에 해당하는 제품을 찾을 수 없습니다.", productNo);
            throw new NullPointerException("Product with productNo " + productNo + " not found");
        }
        increaseViewCount(productNo);

        List<Image> images = imageMapper.findImagesByProductNo(productNo);
        product.setExistingImages(images);
        List<RetrieveProduct> relatedProducts = productMapper.findTopProductsByCategory(product.getCategoryId(), productNo);

        return new RetrieveProductDetailResponse(product, images, relatedProducts);
    }

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getProductsByCategory(String sort, int categoryId, int page, int size) {
        log.info("카테고리별 제품 목록을 가져오는 서비스 - 정렬 기준: {}, 카테고리 ID: {}, 페이지: {}, 사이즈: {}", sort, categoryId, page, size);
        int offset = (page - 1) * size;
        List<RetrieveProduct> products = productMapper.findProductsByCategory(sort, categoryId, offset, size);
        int totalProducts = productMapper.countProductsByCategory(categoryId);
        int totalPages = (totalProducts + size - 1) / size;
        return new RetrieveProductsResponse(products, sort, page, totalPages);
    }

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse getTopProductsByCategory(int categoryId, int productNo) {
        log.info("카테고리별 상위 제품을 조회하는 서비스 - 카테고리 ID: {}", categoryId);
        List<RetrieveProduct> products = productMapper.findTopProductsByCategory(categoryId, productNo);
        return new RetrieveProductsResponse(products, "viewCount_desc", 1, 1);
    }

    @Transactional(readOnly = true)
    @Override
    public RetrieveProductsResponse searchProductByName(String input, String sort, int page, int size) {
        log.info("검색값에 맞는 상품을 가져오는 서비스 - 상품이름: {}, 정렬기준: {}, 페이지: {}, 사이즈: {}", input, sort, page, size);
        int offset = (page - 1) * size;
        List<RetrieveProduct> products = productMapper.findProductByName(input, sort, offset, size);
        int totalProducts = productMapper.countByProductsName(input);
        int totalPages = (totalProducts + size - 1) / size;
        return new RetrieveProductsResponse(products, sort, page, totalPages);
    }

    @Override
    @Transactional
    public void deleteProductWithImages(Long id) {
        int productNo = id.intValue();
        List<Image> images = imageMapper.findImagesByProductNo(productNo);

        // 실제 이미지를 ImageService를 통해 삭제
        for (Image image : images) {
            imageService.deleteImageByUrl(image.getImageName());
        }

        // product_image 테이블에서 관련 행 삭제
        productMapper.deleteProductImages(productNo);

        // product 테이블에서 행 삭제
        productMapper.deleteProduct(productNo);

        // image 테이블에서 이미지 레코드 삭제
        for (Image image : images) {
            imageMapper.deleteImage(image.getImageId());
        }
    }

    @Override
    public List<String> getProductImageUrls(Long id) {
        List<Image> images = imageMapper.findImagesByProductNo(id.intValue());
        return images.stream()
                .map(Image::getImageName)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveProductWithImages(RetrieveProduct product, List<MultipartFile> images) {
        log.info("saveProductWithImages called with product: {}", product.getProductName());

        // 상품을 먼저 저장하여 productNo를 생성
        productMapper.addProduct(product);

        // 중복 이미지를 필터링하여 저장
        List<Image> imageEntities = images.stream()
                .filter(image -> !isDuplicateImage(image, product.getProductNo()))
                .map(image -> {
                    log.info("Processing image: {}", image.getOriginalFilename());
                    Image imageEntity = imageService.convertToImageEntity(image);
                    imageService.saveImage(imageEntity);
                    return imageEntity;
                })
                .collect(Collectors.toList());

        // 첫 번째 이미지를 메인 이미지로 설정
        if (!imageEntities.isEmpty()) {
            product.setMainImage(imageEntities.get(0));
        }

        // 상품을 업데이트하여 메인 이미지 설정
        Map<String, Object> params = new HashMap<>();
        params.put("productNo", product.getProductNo());
        params.put("mainImage", product.getMainImage().getImageId());
        productMapper.updateProductMainImage(params);

        // 제품에 이미지들을 설정하고 product_image 매핑
        for (Image image : imageEntities) {
            productMapper.saveProductImageMapping(product.getProductNo(), image.getImageId());
        }
    }
    @Transactional
    @Override
    public void updateProductWithImages(RetrieveProduct product, List<MultipartFile> newImages, List<Integer> imagesToDelete) {
        log.info("updateProductWithImages called with product: {}", product.getProductName());

        // 기존 상품 정보 업데이트
        productMapper.updateProduct(product);

        // 기존 이미지를 가져옴
        List<Image> existingImages = imageMapper.findImagesByProductNo(product.getProductNo());
        log.info("Existing images: {}", existingImages);

        // 삭제할 이미지가 있을 경우 처리
        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            for (Integer imageId : imagesToDelete) {
                Image image = imageMapper.findImageById(imageId);
                if (image != null) {
                    // 클라우드 스토리지에서 이미지 삭제
                    imageService.deleteImageByUrl(image.getImageName());
                    // 이미지 매핑 삭제
                    productMapper.deleteProductImageMapping(product.getProductNo(), imageId);
                    // 이미지 레코드 삭제
                    imageMapper.deleteImage(imageId);
                }
            }
        }

        // 새 이미지가 있을 경우 처리
        List<Image> newImageEntities = new ArrayList<>();
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile newImage : newImages) {
                if (newImage != null && !newImage.isEmpty()) {  // 파일이 있는 경우에만 처리
                    log.info("Processing new image: {}", newImage.getOriginalFilename());
                    Image imageEntity = imageService.convertToImageEntity(newImage);
                    imageService.saveImage(imageEntity);
                    newImageEntities.add(imageEntity);
                }
            }

            // 새 이미지 매핑 저장
            for (Image image : newImageEntities) {
                productMapper.saveProductImageMapping(product.getProductNo(), image.getImageId());
            }
        }

        // 첫 번째 이미지를 메인 이미지로 설정 (기존 이미지 + 새 이미지 중 첫 번째)
        List<Image> allImages = new ArrayList<>(existingImages);
        allImages.addAll(newImageEntities);
        if (!allImages.isEmpty()) {
            product.setMainImage(allImages.get(0));
        }

        // 메인 이미지 업데이트
        Map<String, Object> params = Map.of(
                "productNo", product.getProductNo(),
                "mainImage", product.getMainImage().getImageId()
        );
        productMapper.updateProductMainImage(params);
    }

    private boolean isDuplicateImage(MultipartFile image, int productNo) {
        List<Image> existingImages = imageMapper.findImagesByProductNo(productNo);
        String fileName = image.getOriginalFilename();
        return existingImages.stream()
                .anyMatch(existingImage -> existingImage.getImageName().endsWith(fileName));
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
