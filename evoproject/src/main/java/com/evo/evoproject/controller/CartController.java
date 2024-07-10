package com.evo.evoproject.controller;

import com.evo.evoproject.model.Cart;
import com.evo.evoproject.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger log = Logger.getLogger(CartController.class.getName());

    @Autowired
    private CartService cartService;

    //장바구니 전체 상품 조회
    @GetMapping("/{userNo}")
    public String viewCart(@PathVariable int userNo, HttpSession session) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        // 모든 장바구니 항목을 세션에 저장
        session.setAttribute("cartItems", cartItems);

       // log.info("사용자 " + userNo + "의 장바구니 항목 조회: " + cartItems.size() + "개의 상품");

        return "cart";
    }


    // 장바구니에서 상품 제거
    @GetMapping("/delete")
    public String deleteProductFromCart(@RequestParam int userNo, @RequestParam int proNo, HttpSession session) {
        cartService.deleteProductFromCart(userNo, proNo);
        log.info("장바구니에서 상품이 제거되었습니다. 상품 번호: " + proNo);

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", updatedCartItems);

        return "redirect:/cart/" + userNo;  // 페이지를 새로고침
    }
}