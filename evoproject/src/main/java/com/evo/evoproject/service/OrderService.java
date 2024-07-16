package com.evo.evoproject.service;

import com.evo.evoproject.model.Order;
import com.evo.evoproject.repository.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 추가

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional // 추가: 트랜잭션 처리
    public void createOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderMapper.findAllOrders();
    }
}
