package com.evo.evoproject.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //HTTP 요청에 대한 권한 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // 특정 경로에 대한 접근 허용
                                .requestMatchers("/", "/index.html", "/header.html", "/footer.html", "/register",
                                        "/login", "/css/**", "/image/**","/js/**",
                                        "/find-id","/find-password","/check-username","/terms","/snb.html").permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN") // 관리자 전용 URL 패턴
                                .anyRequest().authenticated() // 이 외의 요청은 인증 필요
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 커스텀 로그인 페이지 설정
                                .successHandler(customAuthenticationSuccessHandler()) // 성공 핸들러 추가
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // 로그아웃 URL 설정
                                .logoutSuccessUrl("/login") // 로그아웃 성공 시 리다이렉트 URL
                                .permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/403") // 접근 거부 시 이동할 페이지 설정
                                // 인증되지 않은 사용자 처리
                                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .csrf(csrf -> csrf.disable()); // CSRF 보호 비활성화

        return http.build();
    }

}
