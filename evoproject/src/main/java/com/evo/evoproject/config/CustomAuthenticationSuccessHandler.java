package com.evo.evoproject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 로그인 성공 시 호출되는 메소드. 사용자 인증이 성공하면 이 메소드가 실행.
     *
     * @param request        HttpServletRequest 객체
     * @param response       HttpServletResponse 객체
     * @param authentication Authentication 객체 (인증된 사용자 정보를 포함)
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //인증된 사용자 정보 가져오기
        User user = (User) authentication.getPrincipal();
        //세션에 로그인한 사용자 id 저장
        request.getSession().setAttribute("userId", user.getUsername());
        // 루트 페이지로 리다이렉트
        response.sendRedirect("/");
    }
}
