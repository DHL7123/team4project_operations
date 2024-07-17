package com.evo.evoproject.Mapper.order;

import com.evo.evoproject.domain.order.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    List<Order> selectOrderList(Map<String, Object> map);
    int countAllOrders();
    void updateDeliveryState(Order order);
    void insertDeliveryNumber(Order order);
    void updateOrderToCancelRefund(Order order);
}