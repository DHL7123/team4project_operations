package com.evo.evoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String viewCart(Model model) {
        // 모델에 필요한 속성 추가
        // model.addAttribute("cartItems", cartService.getCartItems());
        // model.addAttribute("subtotal", cartService.getSubtotal());

        return "cart";
    }
}


