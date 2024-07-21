package com.evo.evoproject.controller.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.service.cart.CartService;
import com.evo.evoproject.service.order.UserOrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class UserOrderController {

    private final UserOrderService userOrderService;
    private final CartService cartService;

    @GetMapping
    public String getUserOrders(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login";
        }

        log.info("회원의 주문 목록 요청 - 회원번호: {}, 페이지: {}, 사이즈: {}", userNo, page, size);
        try {
            RetrieveOrdersResponse response = userOrderService.getOrdersByUserNo(userNo, page, size);
            model.addAttribute("orders", response.getOrders());
            model.addAttribute("ordersResponse", response);
            model.addAttribute("userNo", userNo);
            return "/order/list";
        } catch (Exception e) {
            log.error("주문 목록을 가져오는 중 오류 발생", e);
            model.addAttribute("error", "주문 목록을 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable int orderId, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login";
        }

        log.info("주문 상세 정보 요청 - 주문 번호: {}, 사용자 번호: {}", orderId, userNo);
        Order order = userOrderService.getOrderDetails(orderId, userNo);
        if (order == null) {
            model.addAttribute("error", "주문 정보를 찾을 수 없습니다.");
            return "error";
        }

        model.addAttribute("order", order);
        return "/order/details";
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRequest orderRequest, HttpSession session, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login";
        }

        log.info("새로운 주문 생성 요청 - 사용자 번호: {}", userNo);
        try {
            userOrderService.createOrder(userNo, orderRequest);
            return "redirect:/orders";
        } catch (Exception e) {
            log.error("주문 생성 중 오류 발생", e);
            model.addAttribute("error", "주문 생성 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable int orderId, Model model) {
        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login";
        }

        log.info("주문 취소 요청 - 주문 번호: {}, 사용자 번호: {}", orderId, userNo);
        try {
            userOrderService.cancelOrder(orderId, userNo);
            return "redirect:/orders";
        } catch (Exception e) {
            log.error("주문 취소 중 오류 발생", e);
            model.addAttribute("error", "주문 취소 중 오류가 발생했습니다.");
            return "error";
        }
    }

    private Integer getCurrentUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String currentUsername = authentication.getName();
            return cartService.getUserNoByUserId(currentUsername);
        }
        return null;
    }
}
