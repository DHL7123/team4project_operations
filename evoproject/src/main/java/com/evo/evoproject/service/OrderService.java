package com.evo.evoproject.service;

import com.evo.evoproject.domain.order.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByStatus(int status);
    List<Order> getAllOrders();
}