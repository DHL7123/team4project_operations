package com.evo.evoproject.mapper.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserOrderMapper {

    List<Order> findOrdersById(@Param("userNo") int userNo, @Param("offset") int offset, @Param("size") int size);

    Order findOrderDetails(@Param("orderId") int orderId, @Param("userNo") int userNo);

    void insertOrder(OrderRequest orderRequest);

    void updateOrderStatus(@Param("orderId") int orderId, @Param("userNo") int userNo, @Param("status") String status);

    int countOrdersByUserNo(@Param("userNo") int userNo);

    Orderitem findOrderItemByProductNo(@Param("productNo") int productNo);

    void updateProductStock(@Param("productNo") int productNo, @Param("quantity") int quantity);
}