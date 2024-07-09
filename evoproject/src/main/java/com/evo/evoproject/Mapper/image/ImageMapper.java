package com.evo.evoproject.Mapper.image;

import com.evo.evoproject.domain.image.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    void insertImage (Image image);
    Image findImageById (int imageId);

}
