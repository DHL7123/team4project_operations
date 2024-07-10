package com.evo.evoproject.repository;

import com.evo.evoproject.model.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartRepository {

    List<Cart> findByUserNo(int userNo);

    void addProductToCart(Cart cart);

    void deleteProductFromCart(int userNo, int proNo);
}