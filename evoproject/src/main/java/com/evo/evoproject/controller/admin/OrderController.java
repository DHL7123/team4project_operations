package com.evo.evoproject.controller.admin;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.service.order.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    private final OrderService orderService;

    public static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 특정 상태에 따른 주문 목록을 조회하고 관리 페이지 반환
     * @param status 조회할 주문 상태 코드
     * @param model Spring MVC의 모델 객체
     * @return 관리 페이지 뷰 이름
     */
    @GetMapping("/admin/manageOrder/{status}")
    public String getOrdersByStatus(@PathVariable int status, @RequestParam(defaultValue = "1") int page, Model model) {
        int limit = 10;
        int offset = (page - 1) * limit;
        List<Order> orders;
        int totalOrders;

        if (status == 5) {
            orders = orderService.getAllOrders(limit, offset);
            totalOrders = orderService.countAllOrders();
        } else {
            orders = orderService.getOrdersByStatus(status, limit, offset);
            totalOrders = orderService.countOrdersByStatus(status);
        }

        int totalPage = (int) Math.ceil((double) totalOrders / limit);

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status == 5 ? "all" : status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("countPending", orderService.countOrdersByStatus(0));
        model.addAttribute("countPreparing", orderService.countOrdersByStatus(1));
        model.addAttribute("countShipping", orderService.countOrdersByStatus(2));
        model.addAttribute("countRequest", orderService.countOrdersByStatus(3));
        model.addAttribute("countCompleted", orderService.countOrdersByStatus(4));
        return "/admin/manageOrder";
    }

    /**
     * 모든 주문 목록을 조회하고 관리 페이지 반환
     * @param model Spring MVC의 모델 객체
     * @return 관리 페이지 뷰 이름
     */
    @GetMapping("/admin/manageOrder")
    public String getAllOrders(@RequestParam(defaultValue = "1") int page, Model model) {
        int limit = 10;
        int offset = (page - 1) * limit;
        List<Order> orders = orderService.getAllOrders(limit, offset);

        int totalOrders = orderService.countAllOrders();
        int totalPage = (int) Math.ceil((double) totalOrders / limit);

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", "all");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("countPending", orderService.countOrdersByStatus(0));
        model.addAttribute("countPreparing", orderService.countOrdersByStatus(1));
        model.addAttribute("countShipping", orderService.countOrdersByStatus(2));
        model.addAttribute("countRequest", orderService.countOrdersByStatus(3));
        model.addAttribute("countCompleted", orderService.countOrdersByStatus(4));
        return "/admin/manageOrder";
    }

    /**
     * 주문 상태를 업데이트하고 이전 페이지로 리다이렉트
     * @param orderNo 업데이트할 주문 번호
     * @param status 업데이트할 주문 상태 코드
     * @param prevStatus 이전 페이지 상태 코드
     * @return 이전 페이지 또는 관리 페이지 리다이렉트 경로
     */
    @PostMapping("/admin/manageOrder/{orderNo}/{status}")
    public String updateOrderStatus(@PathVariable int orderNo, @PathVariable int status, @RequestParam("prevStatus") String prevStatus) {
        orderService.updateOrderStatus(orderNo, status);
        if ("all".equals(prevStatus)) {
            return "redirect:/admin/manageOrder";
        } else {
            return "redirect:/admin/manageOrder/" + prevStatus;
        }
    }

    /**
     * 주문의 배송번호를 업데이트하고 주문 상태를 업데이트한 후 이전 페이지로 리다이렉트
     * @param orderNo 업데이트할 주문 번호
     * @param status 업데이트할 주문 상태 코드
     * @param orderDelivnum 업데이트할 배송번호
     * @param prevStatus 이전 페이지 상태 코드
     * @return 이전 페이지 또는 관리 페이지 리다이렉트 경로
     */
    @PostMapping("/admin/manageOrder/{orderNo}/{status}/orderDelivnum")
    public String updateDelivnum(@PathVariable int orderNo, @PathVariable int status, @RequestParam String orderDelivnum, @RequestParam("prevStatus") String prevStatus, RedirectAttributes redirectAttributes) {
        // 운송장 번호가 숫자인지 확인
        if (orderDelivnum.matches("\\d+")) {
            try {
                orderService.updateDelivnum(orderNo, orderDelivnum);
                orderService.updateOrderStatus(orderNo, status);
            } catch (Exception e) {
                log.error("운송장 번호 또는 주문 상태 업데이트 중 오류가 발생했습니다.", e);
                redirectAttributes.addFlashAttribute("error", "운송장 번호 또는 주문 상태 업데이트 중 오류가 발생했습니다.");
                return "redirect:/admin/manageOrder/1";
            }
        } else {
            log.error("운송장 번호는 숫자여야 합니다.");
            redirectAttributes.addFlashAttribute("error", "운송장 번호는 숫자여야 합니다.");
            return "redirect:/admin/manageOrder/1";
        }

        if ("all".equals(prevStatus)) {
            return "redirect:/admin/manageOrder";
        } else {
            return "redirect:/admin/manageOrder/" + prevStatus;
        }
    }




    /**
     * 주문의 요청 타입과 상태를 업데이트하고 이전 페이지로 리다이렉트
     * @param orderNo 업데이트할 주문 번호
     * @param requestType 업데이트할 요청 타입 코드
     * @param orderStatus 업데이트할 주문 상태 코드
     * @param prevStatus 이전 페이지 상태 코드
     * @return 이전 페이지 또는 관리 페이지 리다이렉트 경로
     */
    @PostMapping("/admin/manageOrder/{orderNo}/requestType/{requestType}/orderStatus/{orderStatus}")
    public String updateRequestTypeAndOrderStatus(@PathVariable int orderNo, @PathVariable int requestType, @PathVariable int orderStatus, @RequestParam("prevStatus") String prevStatus) {
        orderService.updateRequestType(orderNo, requestType);
        orderService.updateOrderStatus(orderNo, orderStatus);
        if ("all".equals(prevStatus)) {
            return "redirect:/admin/manageOrder";
        } else {
            return "redirect:/admin/manageOrder/" + prevStatus;
        }
    }

}