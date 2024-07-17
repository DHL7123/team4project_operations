package com.evo.evoproject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.evo.evoproject.Mapper.order.OrderMapper;
import com.evo.evoproject.domain.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}