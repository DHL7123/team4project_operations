package com.evo.evoproject.service.cart;

import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.mapper.cart.CartMapper;
import com.evo.evoproject.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final UserMapper userMapper;

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
     * @param userNo 사용자 번호
     * @param proNo 상품 번호
     * @param quantity 장바구니 상품 개수
     */
    @Transactional
    @Override
    public void addProductToCart(int userNo, int proNo, int quantity) {
        int stock = cartMapper.getProductStock(proNo); // 제품의 재고
        int currentQuantity = cartMapper.getCartProductQuantity(userNo, proNo); // 현재 장바구니에 담긴 수량
        if (quantity + currentQuantity > stock) {
            throw new IllegalArgumentException("재고 수량을 초과했습니다.");
        }
        cartMapper.addProductToCart(userNo, proNo, quantity);
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


    @Override
    public int getUserNoByUserId(String userId) {
        Integer userNo = userMapper.getUserNoByUserId(userId);
        if (userNo == null) {
            throw new IllegalArgumentException("User not found for username: " + userId);
        }
        return userNo;
    }
}