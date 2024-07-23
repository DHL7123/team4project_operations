package com.evo.evoproject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //로그인 성공 시 호출되는 메소드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증된 사용자 정보 가져오기
        User user = (User) authentication.getPrincipal();
        // 세션에 로그인한 사용자 id 저장
        request.getSession().setAttribute("userId", user.getUsername());

        // 사용자의 권한에 따라 리다이렉트할 URL 설정
        String redirectUrl = "/";
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/admin";
                break;
            }
        }

        // 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}
