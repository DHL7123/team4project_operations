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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{userNo}")
    public String listOrders(@PathVariable int userNo, Model model) {
        List<Order> orders = orderService.getAllOrders(userNo);
        model.addAttribute("userNo", userNo);
        model.addAttribute("orders", orders);
        log.info("Orders for user {}: {}", userNo, orders);
        return "orders";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order, HttpSession session) {
        session.setAttribute("order", order); // 세션에 임시 저장
        log.info("Order in session: {}", order);
        return "redirect:/paymentOrders/checkout"; // 결제 페이지로 리다이렉션
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "createOrder";
    }
    // 추가된 부분
    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestBody PaymentCallbackRequest request, HttpSession session) {
        try {
            Order order = (Order) session.getAttribute("order");
            if (order != null) {
                // 결제 정보 설정
                order.setOrder_payment(request.getAmount()); // 결제 금액 설정
                order.setOrder_comment(request.getOrderComment());//주문요청사항
                order.setOrder_address1(request.getOrderAddress1());
                order.setOrder_address2(request.getOrderAddress2());
                orderService.createOrder(order);

                // 세션에서 주문 정보 제거
                session.removeAttribute("order");
                return ResponseEntity.ok("Order saved successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found in session");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 오류 스택 트레이스를 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
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
                order.setPro_name(product.getProName());
                order.setOrder_timestamp(LocalDateTime.now());
                order.setPro_stock(cart.getCartQuantity());
                order.setOrder_payment(product.getProPrice()); // 기본값 설정
                order.setOrder_status(0); // 기본값 설정
                order.setRequestType(0); // 기본값 설정
                order.setOrder_delivnum(0); // 기본값 설정

                total += product.getProPrice() * cart.getCartQuantity();
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

    @GetMapping("/cart/orderComplete")
    public String showOrderCompletePage(Model model) {
        // 필요한 데이터가 있다면 모델에 추가합니다.
        // 예를 들어, 주문 정보를 가져와서 모델에 추가할 수 있습니다.

        // 이 경우에는 단순히 orderComplete.html을 반환합니다.
        return "orderComplete"; // orderComplete.html을 렌더링
    }


}
