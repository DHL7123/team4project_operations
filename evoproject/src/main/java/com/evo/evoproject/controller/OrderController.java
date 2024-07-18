package com.evo.evoproject.controller;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.order.OrderService;
import com.evo.evoproject.service.product.ProductService;
import com.evo.evoproject.service.user.UserService;
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
@RequestMapping("/paymentOrders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, HttpSession session) {
        session.setAttribute("order", order); // 세션에 임시 저장
        return "redirect:/paymentOrders/checkout"; // 결제 페이지로 리다이렉션
    }

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
            order.setOrder_status(1);
            orderService.createOrder(order);
            session.removeAttribute("order"); // 세션에서 제거
        }

        return "orderComplete";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findUserByUserId(userId);
            model.addAttribute("user", user);
        }
        Order order = (Order) session.getAttribute("order");
        if (order != null) {
            model.addAttribute("order", order);
        }

        return "checkOut";  // checkOut.html 파일과 매핑
    }

}
