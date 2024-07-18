package com.evo.evoproject.controller;

import com.evo.evoproject.domain.order.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @GetMapping("/payment")
    public String showPaymentPage(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order");
        model.addAttribute("order", order);
        return "Payment";
    }
}
