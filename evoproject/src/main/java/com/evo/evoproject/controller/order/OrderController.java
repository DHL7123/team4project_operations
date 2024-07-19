package com.evo.evoproject.controller.order;

import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.service.order.UserOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final UserOrderService userOrderService;

    /**
     * 특정 회원의 주문 목록을 가져오는 메서드
     * @param page       페이지 번호 (기본값: 1)
     * @param size       페이지당 항목 수 (기본값: 10)
     * @param userNo     회원번호
     * @param model      뷰에 데이터를 전달하기 위한 모델 객체
     * @return 카테고리별 제품 목록 뷰 이름
     */
    @GetMapping("/{userNo}")
    public String getOrdersByUserNo(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable int userNo,
            Model model) {
        log.info("회원의 주문 목록 요청 - 회원번호: {}, 페이지: {}, 사이즈: {}", userNo, page, size);
     try {
         RetrieveOrdersResponse response = userOrderService.getOrdersById(userNo, page, size);
         model.addAttribute("orders", response.getOrders());
         model.addAttribute("ordersResponse", response);
         model.addAttribute("UserNo", userNo);
         return "/order/list";
     }catch (Exception e) {
         log.error("제품 목록을 가져오는 중 오류 발생", e);
         model.addAttribute("error", "제품 목록을 가져오는 중 오류가 발생했습니다.");
         return "error";
     }



//    @PostMapping("/save")
//    public String saveOrder(@ModelAttribute Order order, HttpSession session) {
//        session.setAttribute("order", order); // 세션에 임시 저장
//        log.info("Order in session: {}", order);
//        return "redirect:/paymentOrders/checkout"; // 결제 페이지로 리다이렉션
//    }
//
//    @GetMapping
//    public String listOrders(Model model) {
//
//        return "orders";
//    }
//
//    @GetMapping("/create")
//    public String showCreateOrderForm(Model model) {
//        model.addAttribute("order", new Order());
//        return "createOrder";
//    }
//
//    // 추가된 부분
//    @PostMapping("/order/complete")
//    public String completeOrder(HttpSession session) {
//        Order order = (Order) session.getAttribute("order");
//
//        return "orderComplete";
//    }
//
//    @GetMapping("/checkout")
//    public String showCheckoutPage(HttpSession session, Model model) {
////        String userId = (String) session.getAttribute("userId");
////        if (userId != null) {
////            User user = userService.findUserByUserId(userId);
////            model.addAttribute("user", user);
////            log.info("User retrieved from session: {}", user);
////        }
////        // 세션에서 장바구니 정보를 읽어옴
////        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");
////        if (cartItems != null) {
////            model.addAttribute("cartItems", cartItems);
////        }
////
////        Order order = (Order) session.getAttribute("order");
////        if (order != null) {
////            model.addAttribute("order", order);
////
////            log.info("Order retrieved from session: {}", order);
////        } else {
////            log.info("No order found in session.");
////        }
////
//        return "checkOut";  // checkOut.html 파일과 매핑
//    }

    }}


