package com.evo.evoproject.controller.admin;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/manageOrder/{status}")
    public String getOrdersByStatus(@PathVariable int status, Model model) {
        model.addAttribute("orders", orderService.getOrdersByStatus(status));
        model.addAttribute("countPending", orderService.countOrdersByStatus(0));
        model.addAttribute("countPreparing", orderService.countOrdersByStatus(1));
        model.addAttribute("countShipping", orderService.countOrdersByStatus(2));
        model.addAttribute("countRefunding", orderService.countOrdersByStatus(3));
        model.addAttribute("countCompleted", orderService.countOrdersByStatus(4));
        return "/admin/manageOrder";
    }

    @GetMapping("/admin/manageOrder")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("countPending", orderService.countOrdersByStatus(0));
        model.addAttribute("countPreparing", orderService.countOrdersByStatus(1));
        model.addAttribute("countShipping", orderService.countOrdersByStatus(2));
        model.addAttribute("countRefunding", orderService.countOrdersByStatus(3));
        model.addAttribute("countCompleted", orderService.countOrdersByStatus(4));
        return "/admin/manageOrder";
    }
}