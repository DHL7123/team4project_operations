package com.evo.evoproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor).addPathPatterns("/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/productimage/**")
                .addResourceLocations("classpath:/static/productimage/");

        //사용자 환경에 맞춰서 경로 설정합니다.
        registry.addResourceHandler("/boardimage/**")
                .addResourceLocations("file:///D:/EVO/evoproject/src/main/resources/static/boardimage/");
    }
}