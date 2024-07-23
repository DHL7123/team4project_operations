package com.evo.evoproject.controller.main;

import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 장바구니 전체 상품 조회
    @GetMapping
    public String viewCart(HttpServletRequest request, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }

        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        model.addAttribute("userNo", userNo);
        model.addAttribute("cartItems", cartItems);

        log.info("사용자 " + userNo + "의 장바구니 항목 조회: " + cartItems.size() + "개의 상품");

        return "cart";
    }

    // 선택된 상품들을 장바구니에서 삭제
    @PostMapping("/deleteSelected")
    public String deleteSelectedProducts(@RequestParam List<Integer> proNos, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }

        cartService.deleteProductsFromCart(userNo, proNos);
        log.info("장바구니에서 선택된 상품들이 삭제되었습니다.");

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        model.addAttribute("cartItems", updatedCartItems);

        return "redirect:/cart";
    }

    // 장바구니에 상품 추가
    @PostMapping("/add")
    public String addProductToCart(@RequestParam int proNo, @RequestParam int quantity, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }

        cartService.addProductToCart(userNo, proNo, quantity);
        log.info("사용자 " + userNo + "의 장바구니에 상품이 추가되었습니다.");

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        model.addAttribute("cartItems", updatedCartItems);

        return "redirect:/cart";
    }

    // 현재 로그인된 사용자의 userNo를 반환하는 헬퍼 메서드
    private Integer getCurrentUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String currentUsername = authentication.getName();
            return cartService.getUserNoByUserId(currentUsername);
        }
        return null;
    }
}
