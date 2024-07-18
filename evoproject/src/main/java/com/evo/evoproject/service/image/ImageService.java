package com.evo.evoproject.service.image;

import com.evo.evoproject.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    void uploadImages(int productNo, List<MultipartFile> files);
    Image convertToImageEntity(MultipartFile multipartFile);
    void saveImage(Image image);
    void deleteImageByUrl(String imageUrl);
}