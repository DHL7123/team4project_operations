package com.evo.evoproject.controller.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.OrderResponse;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.order.UserOrder;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.cart.CartService;
import com.evo.evoproject.service.order.OrderService;
import com.evo.evoproject.service.order.PaymentOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.evo.evoproject.domain.user.User.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderPaymentController {

    private final PaymentOrderService paymentOrderService;

    private final CartService cartService;

    @GetMapping("/{userNo}")
    public String getOrdersByUserNo(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @PathVariable int userNo,
                                    Model model) {
        log.info("회원의 주문 목록 요청 - 회원번호: {}, 페이지: {}, 사이즈: {}", userNo, page, size);
        try {
            RetrieveOrdersResponse response = paymentOrderService.getOrdersById(userNo, page, size);
            model.addAttribute("orders", response.getOrders());
            model.addAttribute("ordersResponse", response);
            model.addAttribute("userNo", userNo);
            return "/order/list";
        } catch (Exception e) {
            log.error("제품 목록을 가져오는 중 오류 발생", e);
            model.addAttribute("error", "제품 목록을 가져오는 중 오류가 발생했습니다.");
            return "error";
        }
    }

    @PostMapping("/payment")
    public String paymentPage(@RequestParam("productNo") int productNo,
                              @RequestParam("quantity") int quantity,
                              HttpServletRequest request, HttpSession session, Model model) {
        log.info("결제 페이지 요청 - 상품번호: {}, 수량: {}", productNo, quantity);

        Integer userNo = getCurrentUserNo();
        if (userNo == null) {
            return "redirect:/login"; // 로그인 페이지로 리디렉션
        }

        // 사용자 정보를 가져와서 모델에 추가
        User user = paymentOrderService.getUserInfo(userNo);
        if (user == null) {
            log.error("사용자 정보를 찾을 수 없습니다. 사용자 번호: {}", userNo);
            return "error"; // 사용자 정보를 찾을 수 없는 경우 에러 페이지로 리디렉션
        }
        model.addAttribute("user", user);

        // 상품 정보를 가져오는 서비스 호출
        RetrieveOrderItemRequest itemRequest = paymentOrderService.getProductById(productNo);
        if (itemRequest == null) {
            model.addAttribute("error", "해당 상품을 찾을 수 없습니다.");
            return "error";
        }

        // 수량 설정
        itemRequest.setQuantity(quantity);

        // OrderRequest 객체 생성 및 설정
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserNo(userNo);
        orderRequest.setOrderName("Example Order");
        orderRequest.setOrderAddress1(user.getUserAddress1());
        orderRequest.setOrderAddress2(user.getUserAddress2());
        orderRequest.setOrderPhone(user.getUserPhone());
        orderRequest.setOrderComment("No comment");
        orderRequest.setOrderPayment(10000); // 예시 결제 금액
        orderRequest.setItems(List.of(itemRequest));

        // 세션에 orderRequest 저장
        paymentOrderService.storeOrderInSession(orderRequest, session);

        // 모델에 orderRequest 추가
        model.addAttribute("order", orderRequest);

        return "order/orderPayment";
    }

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("paymentInfo") String paymentInfo, HttpSession session, Model model) {
        OrderRequest orderRequest = paymentOrderService.getOrderFromSession(session);
        if (orderRequest == null) {
            return "redirect:/error";
        }

        boolean paymentSuccess = paymentOrderService.processPayment(orderRequest, paymentInfo);
        if (paymentSuccess) {
            // Order 객체로 변환하여 저장
            UserOrder order = new UserOrder();
            order.setUserNo(orderRequest.getUserNo());
            order.setOrderName(orderRequest.getOrderName());
            order.setOrderAddress1(orderRequest.getOrderAddress1());
            order.setOrderAddress2(orderRequest.getOrderAddress2());
            order.setOrderPhone(orderRequest.getOrderPhone());
            order.setOrderComment(orderRequest.getOrderComment());
            order.setOrderPayment(orderRequest.getOrderPayment());
            order.setOrderStatus(orderRequest.getOrderStatus());
            order.setOrderDelivnum(orderRequest.getOrderDelivnum());
            order.setRequestType(orderRequest.getRequestType());

            paymentOrderService.saveOrder(order);

            return "order/paymentSuccess"; // 결제 성공 페이지
        } else {
            return "order/paymentFailure"; // 결제 실패 페이지
        }
    }


    // 현재 로그인된 사용자의 userNo를 반환하는 헬퍼 메서드
    private Integer getCurrentUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String currentUsername = authentication.getName();
            return cartService.getUserNoByUserId(currentUsername);
        }
        return null;
    }
}