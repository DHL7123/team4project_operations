package com.evo.evoproject.domain.product;

import lombok.Data;


@Data
    public class Product {
        private int productNo;
        private String productName;
        private String productDes;
        private String imageId;
        private String categoryId;
        private String price;
        private int stock;
        private String date;
        private int viewCount;


}
// Integer / int
// long <-> int

// new Data(init);

// publi  정적팩토리메서드
// public static Data servciceException(InitData init) {
// new Data(init);
// }


// successfulResponse(initData);




// new Data.builer()
// .id()
// .id()
// .id()
// .id()
// .id()
// .pw()
// ...()
// ..()
// .build();