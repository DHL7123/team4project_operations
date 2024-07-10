package com.evo.evoproject.service;

import com.evo.evoproject.model.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getCartItemsByUser(int userNo);

    void addProductToCart(Cart cart);

    void deleteProductFromCart(int userNo, int proNo);
}