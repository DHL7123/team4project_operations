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

    /**
     * 사용자 번호에 따라 카트 아이템을 조회하는 메서드
     * @param userNo 사용자 번호
     * @return 해당 사용자의 카트 아이템 목록
     */
    @Override
    public List<Cart> getCartItemsByUser(int userNo) {
        return cartRepository.findByUserNo(userNo);
    }

    /**
     * 카트에 상품을 추가하는 메서드
     * @param cart 카트 객체
     */
    @Override
    public void addProductToCart(Cart cart) {
        cartRepository.addProductToCart(cart);
    }

    /**
     * 카트에서 상품을 삭제하는 메서드
     * @param userNo 사용자 번호
     * @param proNo 상품 번호
     */
    @Override
    public void deleteProductFromCart(int userNo, int proNo) {
        cartRepository.deleteProductFromCart(userNo, proNo);
    }
}