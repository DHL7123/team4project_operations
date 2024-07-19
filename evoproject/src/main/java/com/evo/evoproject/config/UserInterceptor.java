package com.evo.evoproject.config;

import com.evo.evoproject.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component
public class UserInterceptor  implements HandlerInterceptor {

    @Autowired
    private CartService cartService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            String currentUserId = authentication.getName();
            int userNo = cartService.getUserNoByUserId(currentUserId);
            request.setAttribute("userNo", userNo);
        } else {
            request.setAttribute("userNo", "guest");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Object userNo = request.getAttribute("userNo");
            if (userNo != null) {
                modelAndView.addObject("userNo", userNo);
            }
        }
    }
}
