package com.evo.evoproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.evo.evoproject.Mapper.order.OrderMapper;
import com.evo.evoproject.domain.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public List<Order> getOrdersByStatus(int status) {
        return orderMapper.getOrdersByStatus(status);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public int countOrdersByStatus(int status) {
        return orderMapper.countOrdersByStatus(status);
    }

    /**
     * 주문의 상태 업데이트
     * @param orderNo 주문 번호
     * @param status 업데이트할 주문 상태 코드
     */
    @Override
    public void updateOrderStatus(int orderNo, int status) {
        orderMapper.updateOrderStatus(orderNo, status);
    }

    /**
     * 주문의 배송번호 업데이트
     * @param orderNo 주문 번호
     * @param orderDelivnum 배송번호
     */
    @Override
    public void updateDelivnum(int orderNo, String orderDelivnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("orderDelivnum", orderDelivnum);
        orderMapper.updateDelivnum(params);
    }

    /**
     * 주문의 요청 타입 업데이트
     * @param orderNo 주문 번호
     * @param requestType 요청 타입 코드
     */
    @Override
    public void updateRequestType(int orderNo, int requestType) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("requestType", requestType);
        orderMapper.updateRequestType(params);
    }
}