// OrderController.java
package com.evo.evoproject.controller.admin;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/manageOrder")
    public String manageOrder(Model model) {
        Map<String, Object> map = new HashMap<>();
        map.put("rowStart", 0);
        map.put("rowEnd", 10);
        List<Order> orders = orderService.selectOrderList(map);
        model.addAttribute("orders", orders);
        return "admin/manageOrder";
    }
}