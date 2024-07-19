package com.evo.evoproject.controller.main;



import com.evo.evoproject.domain.cart.Cart;
import com.evo.evoproject.service.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger log = Logger.getLogger(CartController.class.getName());

    @Autowired
    private CartService cartService;


    //장바구니 전체 상품 조회_model _ model을 사용하여 cart.html에서 접근할 속성들을 추가
    @GetMapping("/{userNo}")
    public String viewCart(@PathVariable int userNo, Model model) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        model.addAttribute("userNo", userNo);
        model.addAttribute("cartItems", cartItems);

        log.info("사용자 " + userNo + "의 장바구니 항목 조회: " + cartItems.size() + "개의 상품");

        return "cart";
    }


    // 장바구니에서 상품 제거 _ 쿼리 파라미터에서 userNo와 proNo를 받아온다.
    @GetMapping("/delete")
    public String deleteProductFromCart(@RequestParam int userNo, @RequestParam int proNo, HttpSession session) {
        cartService.deleteProductFromCart(userNo, proNo);
        log.info("장바구니에서 상품이 제거되었습니다. 상품 번호: " + proNo);

        // 장바구니를 다시 조회 후 업데이트
        List<Cart> updatedCartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", updatedCartItems);

        // 페이지 새로고침
        return "redirect:/cart/" + userNo;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam int userNo, HttpSession session) {
        List<Cart> cartItems = cartService.getCartItemsByUser(userNo);
        session.setAttribute("cartItems", cartItems);
        // 세션에서 장바구니 정보를 읽어와서 로그로 출력
        List<Cart> savedCartItems = (List<Cart>) session.getAttribute("cartItems");
        if (savedCartItems != null) {
            log.info("세션에 저장된 장바구니 정보:");
            for (Cart cart : savedCartItems) {
                log.info("Cart ID: " + cart.getCartId() + ", User No: " + cart.getUserNo() + ", Quantity: " + cart.getCartQuantity() + ", Products: " + cart.getProducts());
            }
        } else {
            log.info("세션에 장바구니 정보가 없습니다.");
        }
        return "redirect:/paymentOrders/checkout";
    }

}