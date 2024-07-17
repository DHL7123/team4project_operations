package com.evo.evoproject.service;

import com.evo.evoproject.domain.order.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Order> selectOrderList(Map<String, Object> map);
    int countAllOrders();
    void updateDeliveryState(Order order);
    void insertDeliveryNumber(Order order);
    void updateOrderToCancelRefund(Order order);


}