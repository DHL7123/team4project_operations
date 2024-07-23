package com.evo.evoproject.controller.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.cart.CartService;
import com.evo.evoproject.service.order.PaymentOrderService;
import com.evo.evoproject.service.order.UserOrderService;
import com.evo.evoproject.service.product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderPaymentController {

    private final PaymentOrderService paymentOrderService;
    private final UserOrderService userOrderService;
    private final CartService cartService;

    @PostMapping("/payment")
    public String paymentPage(@RequestParam("selectedItems") String selectedItemsJson,
                              @RequestParam("proNo") Integer proNo,
                              HttpSession session, Model model) {
        log.info("결제 페이지 요청 - 선택된 상품들: {}", selectedItemsJson);

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

        // 선택된 상품들 처리
        List<RetrieveOrderItemRequest> itemRequests = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> selectedItems;
        try {
            selectedItems = objectMapper.readValue(selectedItemsJson, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            log.error("선택된 상품들 파싱 오류: {}", e.getMessage());
            model.addAttribute("error", "선택된 상품 정보를 파싱하는 중 오류가 발생했습니다.");
            return "error";
        }

        for (Map<String, Object> selectedItem : selectedItems) {
            int productNo;
            int quantity;

            try {
                productNo = Integer.parseInt(selectedItem.get("productNo").toString());
                quantity = Integer.parseInt(selectedItem.get("quantity").toString());
            } catch (NumberFormatException e) {
                log.error("상품 번호 또는 수량 변환 오류: {}", e.getMessage());
                model.addAttribute("error", "상품 번호 또는 수량 변환 중 오류가 발생했습니다.");
                return "error";
            }

            // 상품 정보를 가져오는 서비스 호출
            Orderitem orderItem = paymentOrderService.getProductById(productNo);
            if (orderItem == null) {
                log.error("상품 정보를 찾을 수 없습니다. 상품 번호: {}", productNo);
                model.addAttribute("error", "상품 정보를 찾을 수 없습니다.");
                return "error";
            }

            // Orderitem을 RetrieveOrderItemRequest로 변환하고 수량 설정
            RetrieveOrderItemRequest itemRequest = paymentOrderService.convertToRetrieveOrderItemRequest(orderItem);
            itemRequest.setQuantity(quantity); // 수량 설정
            itemRequests.add(itemRequest);
            log.info("상품 정보 추가 - 아이템: {}", itemRequest);
        }
        model.addAttribute("items", itemRequests);

        // 주문 정보를 생성하고 세션에 저장
        OrderRequest orderRequest = paymentOrderService.createOrderRequest(userNo, itemRequests);
        orderRequest.setProNo(proNo); // proNo 설정
        session.setAttribute("orderRequest", orderRequest); // 세션에 저장
        model.addAttribute("order", orderRequest);
        log.info("주문 정보 생성 및 세션 저장 - 주문: {}", orderRequest);

        // 총 가격 계산 및 모델에 추가
        int totalPrice = itemRequests.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity() + item.getShipping())
                .sum();
        model.addAttribute("totalPrice", totalPrice);

        return "/order/payment";
    }

    @PostMapping("/processPayment")
    public ResponseEntity<String> processPayment(@RequestBody OrderRequest ajaxOrder,
                                                 @RequestParam String paymentInfo,
                                                 HttpSession session) {
        log.info("결제 완료 요청 - 결제 정보: {}", paymentInfo);

        // 세션에서 주문 정보 가져오기
        OrderRequest sessionOrder = (OrderRequest) session.getAttribute("orderRequest");
        if (sessionOrder == null) {
            log.error("세션에서 주문 정보를 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body("세션에서 주문 정보를 찾을 수 없습니다.");
        }
        log.info("세션에서 가져온 주문 정보: {}", sessionOrder);

        // AJAX로 전달된 정보로 세션 주문 정보 업데이트
        sessionOrder.setOrderName(ajaxOrder.getOrderName());
        sessionOrder.setOrderAddress1(ajaxOrder.getOrderAddress1());
        sessionOrder.setOrderAddress2(ajaxOrder.getOrderAddress2());
        sessionOrder.setOrderPhone(ajaxOrder.getOrderPhone());
        sessionOrder.setOrderComment(ajaxOrder.getOrderComment());

        // 결제 처리
        boolean paymentSuccess = paymentOrderService.processPayment(sessionOrder, paymentInfo);
        if (!paymentSuccess) {
            return ResponseEntity.badRequest().body("결제 처리 중 오류가 발생했습니다.");
        }

        // 주문 생성
        try {
            userOrderService.createOrder(sessionOrder.getUserNo(), sessionOrder);
            return ResponseEntity.ok("주문이 완료되었습니다.");
        } catch (Exception e) {
            log.error("주문 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 생성 중 오류가 발생했습니다.");
        }
    }


    @GetMapping("/complete")
    public String orderComplete() {
        return "/order/complete";
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
