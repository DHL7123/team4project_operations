package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderMapper userOrderMapper;

    @Transactional(readOnly = true)
    @Override
    public RetrieveOrdersResponse getOrdersByUserNo(int userNo, int page, int size) {

        int offset = (page - 1) * size;
        int totalOrders = userOrderMapper.countOrdersByUserNo(userNo);
        int totalPages = (totalOrders + size - 1) / size;

        List<Order> orders = userOrderMapper.findOrdersById(userNo, offset, size);

        return new RetrieveOrdersResponse(orders,userNo, page, totalPages);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOrderDetails(int orderId, int userNo) {
        return userOrderMapper.findOrderDetails(orderId, userNo);
    }


    @Transactional
    @Override
    public void createOrder(int userNo, OrderRequest orderRequest) {
        // 주문 생성 로직
        userOrderMapper.insertOrder(orderRequest);
        log.info("주문이 생성되었습니다: {}", orderRequest);

        orderRequest.getItems().forEach(item -> {
            userOrderMapper.updateProductStock(item.getProductNo(), item.getQuantity());
            log.info("재고를 {} 만큼 차감합니다.", item.getQuantity());
        });
    }

    @Transactional
    @Override
    public void cancelOrder(int orderId, int userNo) {
        // 주문 취소 로직
        userOrderMapper.updateOrderStatus(orderId, userNo, "CANCELLED");
        log.info("주문이 취소되었습니다: 주문 번호: {}, 사용자 번호: {}", orderId, userNo);
    }

    @Override
    public int getTotalPages(int userNo, int size) {
        int totalOrders = userOrderMapper.countOrdersByUserNo(userNo);
        return (totalOrders + size - 1) / size;
    }

    @Override
    public OrderRequest getOrderFromSession(HttpSession session) {
        return (OrderRequest) session.getAttribute("order");
    }
}

