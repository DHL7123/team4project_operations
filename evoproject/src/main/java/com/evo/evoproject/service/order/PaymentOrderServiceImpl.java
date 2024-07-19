package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.OrderResponse;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.mapper.order.UserOrderMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final UserOrderMapper userOrderMapper;
    private final RestTemplate restTemplate = new RestTemplate(); // REST API 호출을 위한 RestTemplate

    @Transactional(readOnly = true)
    @Override
    public RetrieveOrdersResponse getOrdersById(int userNo, int page, int size) {
        int offset = (page - 1) * size;

        List<Order> orders = userOrderMapper.findOrdersById(userNo, offset, size);

        int totalOrders = userOrderMapper.countOrdersById(userNo);
        int totalPages = (totalOrders + size - 1) / size;

        return new RetrieveOrdersResponse(orders, page, totalPages);
    }

    @Override
    public void storeOrderInSession(OrderRequest order, HttpSession session) {
        session.setAttribute("order", order);
        log.info("주문 정보가 세션에 저장되었습니다.");
    }

    @Override
    public OrderRequest getOrderFromSession(HttpSession session) {
        OrderRequest order = (OrderRequest) session.getAttribute("order");
        if (order == null) {
            log.error("세션에서 주문 정보를 찾을 수 없습니다.");
        } else {
            log.info("세션에서 주문 정보를 불러왔습니다.");
        }
        return order;
    }

    @Override
    public RetrieveOrderItemRequest getProductById(int productNo) {
        Orderitem orderitem = userOrderMapper.findOrderItemByProductNo(productNo);
        if (orderitem == null) {
            log.error("해당 상품을 찾을 수 없습니다. 상품번호: {}", productNo);
            return null;
        }
        return new RetrieveOrderItemRequest(
                orderitem.getProductNo(),
                orderitem.getProductName(),
                orderitem.getQuantity(),
                orderitem.getPrice(),
                orderitem.getShippingCost(),
                orderitem.getImage()
        );
    }

    @Override
    public boolean processPayment(OrderRequest order, String paymentInfo) {
        log.info("결제 처리를 시작합니다. 결제 정보: {}", paymentInfo);

        // 결제 API 요청을 위한 URL과 요청 데이터 설정
        String paymentApiUrl = "https://api.iamport.co/api/supplements/v1/link/payment";
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderNo", order.getUserNo()); // 실제 결제 API에 맞는 데이터 설정
        paymentRequest.put("paymentInfo", paymentInfo);
        paymentRequest.put("amount", order.getOrderPayment());

        // 결제 API 호출
        try {
            Map<String, Object> response = restTemplate.postForObject(paymentApiUrl, paymentRequest, Map.class);
            boolean paymentSuccess = Boolean.TRUE.equals(response.get("success")); // 응답에서 결제 성공 여부 확인
            log.info("결제 처리 결과: {}", paymentSuccess ? "성공" : "실패");
            return paymentSuccess;
        } catch (Exception e) {
            log.error("결제 처리 중 오류 발생: ", e);
            return false;
        }
    }

    @Override
    public OrderResponse completeOrder(OrderRequest order) {
        log.info("주문을 완료합니다. 주문 정보: {}", order);
        Order newOrder = new Order();
        newOrder.setUserNo(order.getUserNo());
        newOrder.setOrderName(order.getOrderName());
        newOrder.setOrderAddress1(order.getOrderAddress1());
        newOrder.setOrderAddress2(order.getOrderAddress2());
        newOrder.setOrderPhone(order.getOrderPhone());
        newOrder.setOrderComment(order.getOrderComment());
        newOrder.setOrderTimestamp(new Date());
        newOrder.setOrderPayment(order.getOrderPayment());
        newOrder.setOrderStatus(1);
        userOrderMapper.insertOrder(newOrder);

        return new OrderResponse(
                newOrder.getOrderNo(),
                newOrder.getUserNo(),
                newOrder.getOrderName(),
                newOrder.getOrderAddress1(),
                newOrder.getOrderAddress2(),
                newOrder.getOrderPhone(),
                newOrder.getOrderComment(),
                newOrder.getOrderTimestamp(),
                newOrder.getOrderPayment(),
                newOrder.getOrderStatus(),
                newOrder.getOrderDelivnum(),
                newOrder.getRequestType()
        );
    }
}
