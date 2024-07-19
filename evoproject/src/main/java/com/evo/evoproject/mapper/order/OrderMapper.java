package com.evo.evoproject.mapper.order;

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
     * @param limit 한 페이지에 표시할 주문의 수
     * @param offset 결과 집합의 시작점
     * @return 주문 목록
     */
    List<Order> getOrdersByStatus(@Param("status") int status, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 모든 주문 목록 반환
     * @param limit 한 페이지에 표시할 주문의 수
     * @param offset 결과 집합의 시작점
     * @return 모든 주문 목록
     */
    List<Order> getAllOrders(@Param("limit") int limit, @Param("offset") int offset);

    /**
     * 특정 주문 상태에 따른 주문의 수 반환
     * @param status 주문 상태 코드
     * @return 주문 수
     */
    int countOrdersByStatus(int status);

    /**
     * 모든 주문의 수를 반환
     * @return 모든 주문의 수
     */
    int countAllOrders();

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

    List<Order> findOrdersById (@Param("limit") int userNo,@Param("limit") int limit, @Param("offset") int offset);

}