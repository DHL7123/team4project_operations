package com.evo.evoproject.Mapper.order;

import com.evo.evoproject.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> getOrdersByStatus(int status);
    List<Order> getAllOrders();
    int countOrdersByStatus(int status);
    void updateOrderStatus(@Param("orderNo") String orderNo, @Param("status") int status);
    void updateDelivnum(String orderNo, String delivnum);
}