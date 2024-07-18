package com.evo.evoproject.service.order;

import com.evo.evoproject.domain.order.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getOrdersByStatus(int status);
    List<Order> getAllOrders();
    int countOrdersByStatus(int status);
    void updateOrderStatus(int orderNo, int status);
    void updateDelivnum(int orderNo, String orderDelivnum);
    void updateRequestType(int orderNo, int requestType);
}