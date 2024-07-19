package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.OrderResponse;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import jakarta.servlet.http.HttpSession;

public interface PaymentOrderService {

    RetrieveOrdersResponse getOrdersById(int userNo, int page, int size);

    void storeOrderInSession(OrderRequest order, HttpSession session);

    OrderRequest getOrderFromSession(HttpSession session);

    RetrieveOrderItemRequest getProductById(int productNo);

    boolean processPayment(OrderRequest order, String paymentInfo);

    OrderResponse completeOrder(OrderRequest order);
}
