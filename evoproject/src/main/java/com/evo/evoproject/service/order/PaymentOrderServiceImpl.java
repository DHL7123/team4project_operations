package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.OrderResponse;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.order.UserOrder;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.mapper.order.UserOrderMapper;
import com.evo.evoproject.mapper.user.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final UserOrderMapper userOrderMapper;
    private final UserMapper userMapper;

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
            log.info("세션에서 주문 정보를 가져왔습니다.");
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

        // 모의 결제 처리
        boolean paymentSuccess = true; // 모의 결제 성공 처리
        log.info("모의 결제 처리 결과: {}", paymentSuccess ? "성공" : "실패");

        return paymentSuccess;
    }

    public void completeOrder(OrderRequest order) {
        // 주문 완료 처리 로직 추가
        log.info("주문이 완료되었습니다.");
    }
    @Transactional
    @Override
    public void saveOrder(UserOrder order) {
        order.setOrderTimestamp(new Timestamp(System.currentTimeMillis()));
        order.setOrderStatus(0); // 기본값 설정
        order.setRequestType(0); // 기본값 설정

        userOrderMapper.insertOrder(order);

        order.getItems().forEach(item -> {
            userOrderMapper.updateProductStock(item.getProductNo(), item.getQuantity());
        });
    }

    @Override
    public User getUserInfo(int userNo) {
        return userMapper.findUserinfoByUserNo(userNo);
    }
}
