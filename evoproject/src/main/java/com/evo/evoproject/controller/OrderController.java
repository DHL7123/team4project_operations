package com.evo.evoproject.controller;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, HttpSession session) {
        session.setAttribute("order", order); // 세션에 임시 저장
        return "redirect:/payment"; // 결제 페이지로 리다이렉션
    }

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "createOrder";
    }
    // 추가된 부분
    @PostMapping("/order/complete")
    public String completeOrder(HttpSession session) {
        Order order = (Order) session.getAttribute("order");

        if (order != null) {
            order.setStatus("Completed");
            orderService.createOrder(order);
            session.removeAttribute("order"); // 세션에서 제거
        }

        return "orderComplete";
    }

}
