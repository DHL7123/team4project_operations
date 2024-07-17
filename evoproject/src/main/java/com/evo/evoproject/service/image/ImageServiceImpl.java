package com.evo.evoproject.service.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.evo.evoproject.domain.image.Image;
import com.evo.evoproject.mapper.product.ImageMapper;
import com.evo.evoproject.mapper.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 amazonS3;
    private final ImageMapper imageMapper;
    private final ProductMapper productMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public void uploadImages(int productNo, List<MultipartFile> files) {
        List<Image> existingImages = imageMapper.findImagesByProductNo(productNo);
        for (MultipartFile file : files) {
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
    public void deleteImageByUrl(String imageName) {
        String fileName = imageName.substring(imageName.lastIndexOf("/") + 1);
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private boolean isDuplicateImage(MultipartFile file, List<Image> existingImages) {
        String fileName = file.getOriginalFilename();
        return existingImages.stream()
                .anyMatch(existingImage -> existingImage.getImageName().endsWith(fileName));
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 중 오류 발생", e);
        }
        return convFile;
    }

    private String uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File tempFile = convertMultipartFileToFile(file);
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, tempFile));
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}