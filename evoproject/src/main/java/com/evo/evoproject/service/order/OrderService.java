package com.evo.evoproject.service.order;

import com.evo.evoproject.mapper.order.OrderMapper; // 올바른 경로로 import
import com.evo.evoproject.domain.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void createOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderMapper.findAllOrders();
    }

    public Order getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }

    @Transactional
    public void updateOrder(Order order) {
        orderMapper.updateOrder(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderMapper.deleteOrder(id);
    }
}
