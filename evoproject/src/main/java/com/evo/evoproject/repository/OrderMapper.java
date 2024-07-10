package com.evo.evoproject.repository;

import com.evo.evoproject.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (user_name, item, quantity, price) VALUES (#{userName}, #{item}, #{quantity}, #{price})")
    void insertOrder(Order order);

    @Select("SELECT * FROM orders")
    List<Order> findAllOrders();
}
