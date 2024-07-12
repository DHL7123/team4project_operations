package com.evo.evoproject.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 자바스크립트로 알림창을 띄우고 로그인 페이지로 리다이렉트
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String alertMessage = "<script>alert('로그인이 필요한 페이지 입니다.'); window.location='/login';</script>";
        response.getWriter().write(alertMessage);
    }

}
