package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import jakarta.servlet.http.HttpSession;


import java.util.List;

public interface UserOrderService {
    RetrieveOrdersResponse getOrdersByUserNo(int userNo, int page, int size);
    Order getOrderDetails(int orderId, int userNo);
    void createOrder(int userNo, OrderRequest orderRequest);
    void requestCancelOrder(int orderId, int userNo);
    void requestRefundOrder(int orderId, int userNo);
    RetrieveOrdersResponse getCancelReturnsByUserNo(int userNo, int page, int size);
}


