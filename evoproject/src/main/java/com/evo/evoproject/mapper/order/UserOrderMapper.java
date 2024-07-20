package com.evo.evoproject.mapper.order;

import com.evo.evoproject.domain.order.Order;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.order.UserOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserOrderMapper {

    List<Order> findOrdersById(@Param("userNo") int userNo, @Param("offset") int offset, @Param("size") int size);

    int countOrdersById(int userNo);

    void insertOrder(UserOrder order);

    Orderitem findOrderItemByProductNo(int pro_no);

    void updateProductStock(@Param("productNo") int productNo, @Param("quantity") int quantity);
}




