package com.evo.evoproject.service.cart;

import com.evo.evoproject.domain.cart.Cart;

import java.util.List;

public interface CartService {

    List<Cart> getCartItemsByUser(int userNo);

    void addProductToCart(Cart cart);

    void deleteProductsFromCart(int userNo, List<Integer> proNos);
}