package com.evo.evoproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class CartController {

//    @GetMapping("/cart")
//    public String showCart(Model model, HttpSession session) {
//        ArrayList<Product> cartList = (ArrayList<Product>) session.getAttribute("cartlist");
//        if (cartList == null) {
//            cartList = new ArrayList<>();
//        }
//
//        // 계산 로직
//        int sum = 0;
//        for (Product product : cartList) {
//            int total = product.getUnitPrice() * product.getQuantity();
//            sum += total;
//        }
//
//        model.addAttribute("cartList", cartList);
//        model.addAttribute("sum", sum);
//
//        return "cart";
//    }

    @GetMapping("/continue-shopping")
    public String continueShopping() {
//        메인페이지로 포워딩
        return "forward:/main";
    }

    @GetMapping("/login")
    public String showLoginPage() {

        return "login";
    }
}
