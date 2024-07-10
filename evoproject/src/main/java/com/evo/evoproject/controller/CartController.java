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

    @GetMapping("/{userNo}")
    public String viewCart(@PathVariable int userNo, HttpSession session) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", cartItems); // 모든 장바구니 항목을 세션에 저장

        return "cart";
    }


    // 장바구니에 상품 추가
    @PostMapping("/add")
    @ResponseBody
    public String addProductToCart(@RequestBody Cart cart) {
        cartService.addProductToCart(cart);
        log.info("상품이 장바구니에 추가되었습니다. 상품명: " + cart.getProducts().get(0).getProName());
        return "상품이 장바구니에 추가되었습니다";
    }


    // 장바구니에서 상품 제거
    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteProductFromCart(@RequestParam int userNo, @RequestParam int proNo) {
        cartService.deleteProductFromCart(userNo, proNo);
        log.info("장바구니에서 상품이 제거되었습니다. 상품 번호: " + proNo);
        return "장바구니에서 상품이 제거되었습니다";
    }
}