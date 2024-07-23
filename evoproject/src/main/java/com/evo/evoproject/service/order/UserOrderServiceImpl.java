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
    public RetrieveOrdersResponse getOrdersByUserNo(int userNo, int page, int size) {
        int offset = (page - 1) * size;
        int totalOrders = userOrderMapper.countOrdersByUserNo(userNo);
        int totalPages = (totalOrders + size - 1) / size;

        List<Order> orders = userOrderMapper.findOrdersByUserNo(userNo, offset, size);

        return new RetrieveOrdersResponse(orders, userNo, page, totalPages, size);
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
    public void requestCancelOrder(int orderId, int userNo) {
        Order order = userOrderMapper.findOrderByIdAndUserNo(orderId, userNo);
        if (order == null || order.getOrderStatus() != 0) {
            throw new IllegalStateException("주문을 취소할 수 없습니다.");
        }

        order.setRequestType(1); // 결제 취소 요청으로 설정
        userOrderMapper.updateOrderRequestType(order);
    }

    @Transactional
    @Override
    public void requestRefundOrder(int orderId, int userNo) {
        Order order = userOrderMapper.findOrderByIdAndUserNo(orderId, userNo);
        if (order == null || order.getOrderStatus() != 4) {
            throw new IllegalStateException("반품을 요청할 수 없습니다.");
        }

        order.setRequestType(2); // 반품 요청으로 설정
        userOrderMapper.updateOrderRequestType(order);
    }

    @Override
    public RetrieveOrdersResponse getCancelReturnsByUserNo(int userNo, int page, int size) {
        int offset = (page - 1) * size;
        int totalOrders = userOrderMapper.countCancelReturnsByUserNo(userNo);
        int totalPages = (totalOrders + size - 1) / size;

        List<Order> orders = userOrderMapper.findCancelReturnsByUserNo(userNo, offset, size);

        return new RetrieveOrdersResponse(orders, userNo, page, totalPages, size);
    }
}



