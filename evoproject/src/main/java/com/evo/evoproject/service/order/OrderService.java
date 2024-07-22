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

    public List<Order> getAllOrders(Integer user_no) {
        return orderMapper.findAllOrders(user_no);
    }

    public Order getOrderById(int id) {
        return orderMapper.getOrderById(id);
    }

    @Transactional
    public void updateOrder(Order order) {
        orderMapper.updateOrder(order);
    }

    @Transactional
    public void deleteOrder(int id) {
        orderMapper.deleteOrder(id);
    }
}
