package com.evo.evoproject.mapper.product;

import com.evo.evoproject.domain.image.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    void insertImage (Image image);
    Image findImageById (int imageId);
    List<Image> findImagesByProductNo(@Param("productNo") int productNo);
}

