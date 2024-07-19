package com.evo.evoproject.controller.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.OrderResponse;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.service.order.PaymentOrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderPaymentController {

    private final PaymentOrderService paymentOrderService;

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
                              HttpSession session, Model model) {
        log.info("Received productNo: {}", productNo);
        log.info("Received quantity: {}", quantity);

        if (productNo <= 0 || quantity <= 0) {
            log.error("Invalid product number or quantity: productNo={}, quantity={}", productNo, quantity);
            return "redirect:/error";
        }

        RetrieveOrderItemRequest product = paymentOrderService.getProductById(productNo);
        if (product == null) {
            log.error("Product not found: productNo={}", productNo);
            return "redirect:/error";
        }

        OrderRequest order = new OrderRequest(
                1, // userNo 예시 값
                "주문자 이름",
                "주소1",
                "주소2",
                010-1234-5678,
                "주문 코멘트",
                10000, // 예시 결제 금액
                List.of(new RetrieveOrderItemRequest(
                        product.getProductNo(),
                        product.getProductName(),
                        quantity,
                        product.getPrice(),
                        product.getShipping(),
                        product.getMainImage()
                ))
        );

        paymentOrderService.storeOrderInSession(order, session);
        model.addAttribute("order", order);

        return "order/orderPayment"; // 템플릿 경로 수정
    }

    @PostMapping("/processPayment")
    public String processPayment(@RequestParam("paymentInfo") String paymentInfo, HttpSession session, Model model) {
        OrderRequest order = paymentOrderService.getOrderFromSession(session);
        if (order == null) {
            return "redirect:/error";
        }

        boolean paymentSuccess = paymentOrderService.processPayment(order, paymentInfo);
        if (paymentSuccess) {
            paymentOrderService.completeOrder(order);
            return "order/paymentSuccess"; // 결제 성공 페이지
        } else {
            return "order/paymentFailure"; // 결제 실패 페이지
        }
    }

    @GetMapping("/paymentPage")
    public String paymentPage(HttpSession session, Model model) {
        OrderRequest order = paymentOrderService.getOrderFromSession(session);
        if (order == null) {
            return "redirect:/error";
        }
        model.addAttribute("order", order);
        return "order/orderPayment";
    }
}
