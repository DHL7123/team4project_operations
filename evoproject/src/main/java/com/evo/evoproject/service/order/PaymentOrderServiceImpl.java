package com.evo.evoproject.service.order;

import com.evo.evoproject.controller.order.dto.OrderRequest;
import com.evo.evoproject.controller.order.dto.RetrieveOrderItemRequest;
import com.evo.evoproject.domain.order.Orderitem;
import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.mapper.order.UserOrderMapper;
import com.evo.evoproject.mapper.user.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final UserOrderMapper userOrderMapper;
    private final UserMapper userMapper;

    @Override
    public void storeOrderInSession(OrderRequest order, HttpSession session) {
        log.info("Storing order in session: {}", order);
        session.setAttribute("order", order);
        log.info("주문 정보가 세션에 저장되었습니다.");
    }

    @Override
    public OrderRequest getOrderFromSession(HttpSession session) {
        OrderRequest order = (OrderRequest) session.getAttribute("order");
        if (order == null) {
            log.error("세션에서 주문 정보를 찾을 수 없습니다.");
        } else {
            log.info("세션에서 주문 정보를 가져왔습니다: {}", order);
        }
        return order;
    }

    @Override
    public Orderitem getProductById(int productNo) {
        Orderitem orderitem = userOrderMapper.findOrderItemByProductNo(productNo);
        if (orderitem == null) {
            log.error("해당 상품을 찾을 수 없습니다. 상품번호: {}", productNo);
            return null;
        }
        return orderitem;
    }

    @Override
    public boolean processPayment(OrderRequest order, String paymentInfo) {
        log.info("결제 처리를 시작합니다. 결제 정보: {}", paymentInfo);

        // 주문 아이템 확인
        if (order.getItems() == null || order.getItems().isEmpty()) {
            log.error("주문 아이템이 없습니다.");
            return false;
        }

        // 결제 처리 로직 추가 필요
        // 예: 결제 API 호출, 결과 처리 등
        return true; // 임시 반환값
    }

    @Override
    public User getUserInfo(int userNo) {
        User user = userMapper.findUserinfoByUserNo(userNo);
        if (user == null) {
            log.error("사용자 정보를 찾을 수 없습니다. 사용자 번호: {}", userNo);
        } else {
            log.info("사용자 정보를 가져왔습니다: {}", user);
        }
        return user;
    }

    @Override
    public OrderRequest createOrderRequest(int userNo, List<RetrieveOrderItemRequest> itemRequests) {
        int totalPayment = 0;
        int totalQuantity = 0;
        int proNo = 0;

        for (RetrieveOrderItemRequest itemRequest : itemRequests) {
            totalPayment += itemRequest.getPrice() * itemRequest.getQuantity() + itemRequest.getShipping();
            totalQuantity += itemRequest.getQuantity();
            proNo = itemRequest.getProductNo(); // 첫 번째 상품의 proNo로 설정
        }

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserNo(userNo);
        orderRequest.setItems(itemRequests);
        orderRequest.setOrderPayment(totalPayment);
        orderRequest.setQuantity(totalQuantity);
        orderRequest.setProNo(proNo); // proNo 설정
        log.info("주문 요청 생성 - 주문: {}", orderRequest);
        return orderRequest;
    }


    @Override
    public RetrieveOrderItemRequest convertToRetrieveOrderItemRequest(Orderitem orderItem) {
        return new RetrieveOrderItemRequest(
                orderItem.getProductNo(),
                0, // 초기 수량은 0으로 설정하고 이후에 수정
                orderItem.getPrice(),
                orderItem.getShipping(),
                orderItem.getProductName(),
                orderItem.getMainImage()
        );
    }
}
