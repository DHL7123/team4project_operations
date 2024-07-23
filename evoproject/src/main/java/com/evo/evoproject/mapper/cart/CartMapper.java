package com.evo.evoproject.mapper.cart;

import com.evo.evoproject.domain.cart.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByUserNo(int userNo);

    void addProductToCart(int userNo, int proNo, int quantity);

    void deleteProductsFromCart(int userNo, List<Integer> proNos);

    int getProductStock(int proNo);

    int getCartProductQuantity(int userNo, int proNo);

    void updateProductQuantityInCart(int userNo, int proNo, int quantity);
}