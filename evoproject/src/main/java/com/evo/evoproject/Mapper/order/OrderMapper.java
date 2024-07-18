package com.evo.evoproject.Mapper.order;

import com.evo.evoproject.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 특정 주문 상태에 따른 주문 목록 반환
     * @param status 주문 상태 코드
     * @return 주문 목록
     */
    List<Order> getOrdersByStatus(int status);

    /**
     * 모든 주문 목록 반환
     * @return 모든 주문 목록
     */
    List<Order> getAllOrders();

    /**
     * 특정 주문 상태에 따른 주문의 수 반환
     * @param status 주문 상태 코드
     * @return 주문 수
     */
    int countOrdersByStatus(int status);

    /**
     * 주문 상태 업데이트
     * @param orderNo 주문 번호
     * @param status 업데이트할 주문 상태 코드
     */
    void updateOrderStatus(@Param("orderNo") int orderNo, @Param("status") int status);

    /**
     * 주문의 배송번호 업데이트
     * @param params 업데이트할 파라미터 (orderNo와 orderDelivnum)
     */
    void updateDelivnum(Map<String, Object> params);

    /**
     * 주문의 요청 타입 업데이트
     * @param params 업데이트할 파라미터 (orderNo와 requestType)
     */
    void updateRequestType(Map<String, Object> params);
}