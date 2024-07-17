package com.evo.evoproject.domain.image;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class Image {

    private int imageId;
    private String imageName;
    private String imageType;

}

