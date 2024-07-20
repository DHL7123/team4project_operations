package com.evo.evoproject.service.image;

import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.mapper.product.ImageMapper;
import com.evo.evoproject.mapper.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageMapper imageMapper;
    private final ProductMapper productMapper;

    @Value("${image.upload.dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostConstruct
    public void init() {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 중 오류 발생: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void uploadImages(int productNo, List<MultipartFile> files) {
        List<Image> existingImages = imageMapper.findImagesByProductNo(productNo);
        for (MultipartFile file : files) {
            log.info("Uploading file: {}", file.getOriginalFilename());
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalStateException("File size exceeds 5MB");
            }
            if (!isDuplicateImage(file, existingImages)) {
                Image imageEntity = convertToImageEntity(file);
                saveImage(imageEntity);
                productMapper.saveProductImageMapping(productNo, imageEntity.getImageId());
            }
        }
    }

    @Override
    public Image convertToImageEntity(MultipartFile multipartFile) {
        String imageUrl = uploadImage(multipartFile);
        return Image.builder()
                .imageName(imageUrl)
                .imageType("상품")
                .build();
    }

    @Override
    public void saveImage(Image image) {
        log.info("Saving image: {}", image.getImageName());
        imageMapper.saveImage(image);
    }

    @Override
    public void deleteImageByUrl(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        try {
            Path path = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource loadImage(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found " + fileName, e);
        }
    }

    private boolean isDuplicateImage(MultipartFile file, List<Image> existingImages) {
        String fileName = file.getOriginalFilename();
        return existingImages.stream()
                .anyMatch(existingImage -> existingImage.getImageName().endsWith(fileName));
    }

    private String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);
        try {
            log.info("Saving file to: {}", path.toString());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return "/productimage/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
