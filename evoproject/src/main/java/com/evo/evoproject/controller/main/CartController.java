package com.evo.evoproject.controller.main;


import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.service.cart.CartService;
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


    //장바구니 전체 상품 조회_model _ model을 사용하여 cart.html에서 접근할 속성들을 추가
    @GetMapping("/{userNo}")
    public String viewCart(@PathVariable int userNo, Model model) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        model.addAttribute("userNo", userNo);
        model.addAttribute("cartItems", cartItems);

        log.info("사용자 " + userNo + "의 장바구니 항목 조회: " + cartItems.size() + "개의 상품");

        return "cart";
    }

    // 선택된 상품들을 장바구니에서 삭제
    @PostMapping("/deleteSelected")
    public String deleteSelectedProducts(@RequestParam int userNo, @RequestParam List<Integer> proNos, HttpSession session) {
        cartService.deleteProductsFromCart(userNo, proNos);
        log.info("장바구니에서 선택된 상품들이 삭제되었습니다.");

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", updatedCartItems);

        // 페이지 새로고침
        return "redirect:/cart/" + userNo;
    }


    // 장바구니에 상품 추가
    @PostMapping("/add")
    public String addProductToCart(@RequestParam int userNo, @RequestParam int proNo, @RequestParam int quantity, HttpSession session) {
        cartService.addProductToCart(userNo, proNo, quantity);
        log.info("사용자 " + userNo + "의 장바구니에 상품이 추가되었습니다.");

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", updatedCartItems);

        // 장바구니 페이지로 리디렉션
        return "redirect:/cart/" + userNo;
    }
}

