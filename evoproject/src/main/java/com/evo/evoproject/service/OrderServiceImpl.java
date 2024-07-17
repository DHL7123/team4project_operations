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

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> selectOrderList(Map<String, Object> map) {
        return orderMapper.selectOrderList(map);
    }

    @Override
    public int countAllOrders() {
        return orderMapper.countAllOrders();
    }

    @Override
    public void updateDeliveryState(Order order) {
        orderMapper.updateDeliveryState(order);
    }

    @Override
    public void insertDeliveryNumber(Order order) {
        orderMapper.insertDeliveryNumber(order);
    }

    @Override
    public void updateOrderToCancelRefund(Order order) {
        orderMapper.updateOrderToCancelRefund(order);
    }

}