package com.evo.evoproject.controller;

import com.evo.evoproject.model.Order;
import com.evo.evoproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/create")//상품 주문 신청 시 작동하도록 변경해야함. 해당 상품의 정보들을 추가하도록 작성
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "createOrder";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.createOrder(order); // 주문 저장
        return "redirect:/orders"; // 주문 목록으로 리다이렉션
    }
}
