package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.RetrieveOrdersResponse;
import com.evo.evoproject.controller.product.dto.RetrieveProductsResponse;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.product.Product;
import com.evo.evoproject.mapper.order.UserOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderMapper UserOrderMapper;

    @Transactional(readOnly = true)
    @Override
    public RetrieveOrdersResponse getOrdersById(int userNo, int page, int size) {
        int offset = (page - 1) * size;

        List<Order> orders = UserOrderMapper.findOrdersById(userNo, offset, size);

        int totalOrders = UserOrderMapper.countOrdersById(userNo);
        int totalPages = (totalOrders + size - 1) / size;

        return new RetrieveOrdersResponse(orders, page, totalPages);
    }
}

