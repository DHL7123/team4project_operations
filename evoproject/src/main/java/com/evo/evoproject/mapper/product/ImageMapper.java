package com.evo.evoproject.mapper.product;

import com.evo.evoproject.domain.image.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    void saveImage(Image image);
    List<Image> findImagesByProductNo(@Param("productNo") int productNo);
    void deleteImage(int imageId);
}
