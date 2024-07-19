package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.domain.order.Order;

import java.util.List;

public interface UserOrderService {

    RetrieveOrdersResponse getOrdersById(int userNo, int page, int size);
}
