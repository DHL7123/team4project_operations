package com.evo.evoproject.mapper.order;

import com.evo.evoproject.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(Order order);
    List<Order> findAllOrders(int user_no);
    Order getOrderById(int id);
    void updateOrder(Order order);
    void deleteOrder(int id);
}
