package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.user.User;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PaymentOrderService {

    void storeOrderInSession(OrderRequest order, HttpSession session);

    OrderRequest getOrderFromSession(HttpSession session);

    Orderitem getProductById(int productNo);

    boolean processPayment(OrderRequest order, String paymentInfo);

    User getUserInfo(int userNo);

    OrderRequest createOrderRequest(int userNo, List<RetrieveOrderItemRequest> itemRequests);

    RetrieveOrderItemRequest convertToRetrieveOrderItemRequest(Orderitem orderItem);


}
