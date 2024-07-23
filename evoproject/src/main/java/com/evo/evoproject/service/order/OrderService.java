package com.evo.evoproject.service.order;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.user.User;

import java.util.List;
import java.util.Map;


public interface OrderService {
    List<Order> getOrdersByStatus(int status, int limit, int offset);
    List<Order> getAllOrders(int limit, int offset);
    int countOrdersByStatus(int status);
    int countAllOrders();
    void updateOrderStatus(int orderNo, int status);
    void updateDelivnum(int orderNo, String orderDelivnum);
    void updateRequestType(int orderNo, int requestType);

}