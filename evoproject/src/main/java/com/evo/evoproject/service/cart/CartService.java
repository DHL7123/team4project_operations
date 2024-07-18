package com.evo.evoproject.service.cart;

import com.evo.evoproject.domain.cart.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getCartItemsByUser(int userNo);

    void addProductToCart(int userNo, int proNo, int quantity);

    void deleteProductsFromCart(int userNo, List<Integer> proNos);
}