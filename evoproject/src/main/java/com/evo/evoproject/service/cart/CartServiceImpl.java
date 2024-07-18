package com.evo.evoproject.service.cart;

import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.mapper.cart.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;

    /**
     * 사용자 번호에 따라 카트 아이템을 조회하는 메서드
     * @param userNo 사용자 번호
     * @return 해당 사용자의 카트 아이템 목록
     */
    @Transactional(readOnly = true)
    @Override
    public List<Cart> getCartItemsByUser(int userNo) {
        return cartMapper.findByUserNo(userNo);
    }

    /**
     * 카트에 상품을 추가하는 메서드
     * @param cart 카트 객체
     */
    @Transactional
    @Override
    public void addProductToCart(Cart cart) {
        cartMapper.addProductToCart(cart);
    }


    /**
     * 카트에서 여러 상품을 삭제하는 메서드
     * @param userNo 사용자 번호 (User number)
     * @param proNos 삭제할 상품 번호 목록 (List of product numbers to delete)
     */
    @Transactional
    @Override
    public void deleteProductsFromCart(int userNo, List<Integer> proNos) {
        cartMapper.deleteProductsFromCart(userNo, proNos);
    }
}