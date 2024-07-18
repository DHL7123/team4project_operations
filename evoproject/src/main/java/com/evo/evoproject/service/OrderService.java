package com.evo.evoproject.service;

import com.evo.evoproject.domain.order.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getOrdersByStatus(int status);
    List<Order> getAllOrders();
    int countOrdersByStatus(int status);
    void updateOrderStatus(String orderNo, int status);
    void updateDelivnum(Map<String, String> params);
}