package com.evo.evoproject.controller;

import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.domain.cart.Product;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.cart.CartService;
import com.evo.evoproject.service.order.OrderService;
import com.evo.evoproject.service.product.ProductService;
import com.evo.evoproject.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/paymentOrders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, HttpSession session) {
        session.setAttribute("order", order); // 세션에 임시 저장
        log.info("Order in session: {}", order);
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

    @PostMapping("/cart")
    public String checkout(@RequestParam int userNo, HttpSession session) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", cartItems);

        String userId = (String) session.getAttribute("userId");
        User user = userService.findUserByUserId(userId);
        int total = 0;
        for (Cart cart : cartItems) {
            for (Product product : cart.getProducts()) {
                // Order 객체 생성 및 설정
                Order order = new Order();
                order.setUser_no(user.getUserNo());
                order.setPro_no(product.getProNo());
                order.setOrder_name(user.getUserName());
                order.setOrder_address1(user.getUserAddress1());
                order.setOrder_address2(user.getUserAddress2());
                order.setOrder_phone(user.getUserPhone());
                order.setOrder_comment("Example comment"); // 필요 시 설정
                order.setPro_name(product.getProName());
                order.setOrder_timestamp(LocalDateTime.now());
                order.setPro_stock(cart.getCartQuantity());
                order.setOrder_payment(product.getProPrice()); // 기본값 설정
                order.setOrder_status(0); // 기본값 설정
                order.setRequestType(0); // 기본값 설정
                order.setOrder_delivnum(0); // 기본값 설정

                total += product.getProPrice();
                total += product.getShipping();
                // 세션에 저장
                session.setAttribute("order", order);

                // 로그로 확인
                log.info("세션에 저장된 주문 정보 순서대로: {} {} {} {} {} {} {} {} {} {} {} {} {} {}", order.getUser_no(),
                        order.getPro_no(), order.getOrder_name(), order.getOrder_address1(), order.getOrder_address2(),
                        order.getOrder_phone(), order.getOrder_comment(), order.getPro_name(),
                        order.getOrder_timestamp(), order.getPro_stock(), order.getOrder_payment(), order.getOrder_status(), order.getRequestType(), order.getOrder_delivnum());
            }
        }
        session.setAttribute("totalAmount", total);
        log.info("total을 보자: {}", total);
        return "redirect:/paymentOrders/cart/checkout";
    }

    @GetMapping("/cart/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId != null) {
            User user = userService.findUserByUserId(userId);
            model.addAttribute("user", user);
            log.info("User retrieved from session: {}", user);
        }
        // 세션에서 장바구니 정보를 읽어옴
        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
        if (cartItems != null) {
            model.addAttribute("cartItems", cartItems);
        }

        Order order = (Order) session.getAttribute("order");
        if (order != null) {
            model.addAttribute("order", order);

            log.info("Order retrieved from session: {}", order);
        } else {
            log.info("No order found in session.");
        }
        int k = (int) session.getAttribute("totalAmount");
        model.addAttribute("totalAmount", k);
        log.info("total {}", k);

        return "checkOut";  // checkOut.html 파일과 매핑
    }


}
