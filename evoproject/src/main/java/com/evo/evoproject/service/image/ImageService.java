package com.evo.evoproject.service.image;


import com.evo.evoproject.mapper.product.ImageMapper;
import com.evo.evoproject.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private S3Service s3Service;
    private ImageMapper imageMapper;

    public Image uploadImage(MultipartFile file) {
        String imageUrl = s3Service.uploadFile(file);
        Image image = new Image();
        image.setImageName(imageUrl);
        image.setImageType(file.getContentType());
        imageMapper.insertImage(image);
        return image;
    }

    public Image getImageById(int id) {
        return imageMapper.findImageById(id);
    }
}
