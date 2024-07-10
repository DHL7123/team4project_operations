package com.evo.evoproject.service;

import com.evo.evoproject.model.Cart;
import com.evo.evoproject.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> getCartItemsByUser(int userNo) {
        return cartRepository.findByUserNo(userNo);
    }

    @Override
    public void addProductToCart(Cart cart) {
        cartRepository.addProductToCart(cart);
    }

    @Override
    public void deleteProductFromCart(int userNo, int proNo) {
        cartRepository.deleteProductFromCart(userNo, proNo);
    }
}