package com.evo.evoproject.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.evo.evoproject.repository")
public class MybatisConfig {

}