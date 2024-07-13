package com.evo.evoproject.Mapper.cart;

import com.evo.evoproject.domain.cart.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> findByUserNo(int userNo);

    void addProductToCart(Cart cart);

    void deleteProductsFromCart(int userNo, List<Integer> proNos);
}