package com.evo.evoproject.service;

import com.evo.evoproject.model.Order;
import com.evo.evoproject.repository.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public void createOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderMapper.findAllOrders();
    }
}
